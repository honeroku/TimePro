package org.honeroku.timepro.domain.repository;

import android.content.Context;
import android.content.SharedPreferences;

import org.honeroku.timepro.domain.constraint.AccountConstraint;
import org.honeroku.timepro.domain.entity.Account;

public class AccountRepository {

    private static final String PREFERENCE_FILENAME  = "timepro_account";
    private static final String KEY_NAME_DOMAIN      = "domain";
    private static final String KEY_NAME_USER_ID     = "user_id";
    private static final String KEY_NAME_PASSWORD    = "password";

    public static void save(Context context, Account account) {
        assert new AccountConstraint().isSatisfiedBy(account);

        SharedPreferences prefs = getSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_NAME_DOMAIN  , account.getDomain());
        editor.putString(KEY_NAME_USER_ID , account.getUserId());
        editor.putString(KEY_NAME_PASSWORD, account.getPassword());
        editor.apply();
    }

    public static Account load(Context context) {
        SharedPreferences prefs = getSharedPreferences(context);
        String domain   = prefs.getString(KEY_NAME_DOMAIN  , null);
        String userId   = prefs.getString(KEY_NAME_USER_ID , null);
        String password = prefs.getString(KEY_NAME_PASSWORD, null);
        if (domain == null || userId == null || password == null) {
            return null;
        }
        return Account.create(domain, userId, password);
    }

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(PREFERENCE_FILENAME, Context.MODE_PRIVATE);
    }

}
