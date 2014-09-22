package org.honeroku.timepro.infrastructure;

import android.util.Log;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpCookie;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TimeProClient {

    private static final String TAG = TimeProClient.class.getName();
    private static final String USERAGENT =
            "Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.1; Trident/6.0)";
    private static final int REQUEST_INTERVAL = 1000;

    private String domain;
    private OkHttpClient client;
    private Error error;
    private List<HttpCookie> cookies;

    public TimeProClient(String domain) {
        this.domain = domain;
        this.client = new OkHttpClient();
        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        this.client.setCookieHandler(cookieManager);
    }

    private boolean call(Request request) {
        Response response;
        String responseBody;
        try {
            response = client.newCall(request).execute();
            responseBody   = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
            this.error = Error.UNKNOWN_ERROR;
            return false;
        }

        if (!response.isSuccessful()) {
            this.error = Error.valueOf(response.code());
            return false;
        }

        String timeProErrorId = getTimeProErrorId(responseBody);
        if (timeProErrorId != null) {
            this.error = Error.valueOf(Integer.parseInt(timeProErrorId));
            return false;
        }

        this.error = null;
        return true;
    }

    public boolean openNewSession() {
        Log.d(TAG, "Opening new session");
        if (!call(makeGetRequest(TimeProPath.LOGIN))) return false;

        cookies = ((CookieManager) client.getCookieHandler()).getCookieStore().getCookies();
        return true;
    }

    public boolean login(String loginId, String password) {
        if (!openNewSession()) return false;

        sleep(REQUEST_INTERVAL);
        Log.d(TAG, "Logging in");
        RequestBody formBody = new FormEncodingBuilder()
                .add("LoginID"   , loginId )
                .add("PassWord"  , password)
                .add("PROSESS"   , "REFER" )
                .add("PAGESTATUS", "REFER" )
                .add("ERRBACK"   , ""      )
                .build();
        return call(makePostRequest(TimeProPath.LOGIN, formBody));
    }

    public boolean validate(String loginId, String password) {
        if (!login(loginId, password)) return false;

        sleep(REQUEST_INTERVAL);
        Log.d(TAG, "Validating");
        return call(makeGetRequest(TimeProPath.VALIDATE));
    }

    public boolean getMenu(String loginId, String password) {
        if (!validate(loginId, password)) return false;

        sleep(REQUEST_INTERVAL);
        Log.d(TAG, "Getting main menu");
        return call(makeGetRequest(TimeProPath.MENU));
    }

    public boolean clockIn(String loginId, String password) {
        if (!getMenu(loginId, password)) return false;

        sleep(REQUEST_INTERVAL);
        Log.d(TAG, "Clocking in");
        return call(makeGetRequest(TimeProPath.CLOCK_IN));
    }

    public boolean clockOut(String loginId, String password) {
        if (!getMenu(loginId, password)) return false;

        sleep(REQUEST_INTERVAL);
        Log.d(TAG, "Clocking out");
        return call(makeGetRequest(TimeProPath.CLOCK_OUT));
    }

    public String getErrorMessage() {
        return error.getMessage();
    }

    private void sleep(int mSec) {
        try {
            Thread.sleep(mSec);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // FIXME: 汚い
    private String getTimeProErrorId(String responseBody) {
        Pattern errorIdPattern = Pattern.compile("switch \\((2|4)\\)");
        Matcher errorIdMatcher = errorIdPattern.matcher(responseBody);
        if (errorIdMatcher.find()){
            Log.d(TAG, "TimePro Error ID: " + errorIdMatcher.group(1));
            return errorIdMatcher.group(1);
        }

        Pattern invalidCookiePattern = Pattern.compile("再度ログイン");
        Matcher invalidCookieMatcher = invalidCookiePattern.matcher(responseBody);
        if (invalidCookieMatcher.find()){
            return "99";
        }
        return null;
    }

    private Request.Builder makeRequestBuilder(TimeProPath path) {
        Request.Builder builder = new Request.Builder()
                .url(path.getUrlWith(domain))
                .addHeader("User-Agent", USERAGENT);
        if (cookies != null) {
            // FIXME: CookieManagerがHeaderにセットしてくれない
            for (HttpCookie cookie : cookies) {
                builder.addHeader("Cookie", cookie.getName() + "=" + cookie.getValue());
            }
        }
        return builder;
    }

    private Request makeGetRequest(TimeProPath path) {
        return makeRequestBuilder(path).get().build();
    }

    private Request makePostRequest(TimeProPath path, RequestBody formBody) {
        return makeRequestBuilder(path).post(formBody).build();
    }

    private enum TimeProPath {
        LOGIN(    "/xgweb/Login.asp"),
        VALIDATE( "/xgweb/Xgw0000.asp"),
        MENU(     "/xgweb/page/XgwTopMenu.asp"),
        CLOCK_IN( "/xgweb/page/XgwTopMenuClockUpd.asp?PunchKind=1"),
        CLOCK_OUT("/xgweb/page/XgwTopMenuClockUpd.asp?PunchKind=2");

        private final String path;

        private TimeProPath(String path) {
            this.path = path;
        }

        public String getUrlWith(String domain) {
            try {
                return new URI(domain).resolve(path).toString();
            } catch (URISyntaxException e) {
                e.printStackTrace();
                return "";
            }
        }
    }

    // FIXME: 汚い
    public enum Error {
        INVALID_ACCOUNT(2, "個人コードが登録されていません。"),
        INVALID_PASSWORD(4, "パスワードが違います。"),
        INVALID_COOKIE(99, "再度ログインしてください。"),
        UNKNOWN_ERROR(-1, "不明なエラーです。"),
        NOT_FOUND(404, "ページが見つかりません。");

        private int timeProErrorId;
        private String message;

        private Error(int timeProErrorId, String message) {
            this.timeProErrorId = timeProErrorId;
            this.message        = message;
        }

        public static Error valueOf(int timeProErrorId) {
            for (Error error : values()) {
                if (error.timeProErrorId == timeProErrorId) {
                    return error;
                }
            }
            return UNKNOWN_ERROR;
        }

        public String getMessage() {
            return message;
        }
    }

}
