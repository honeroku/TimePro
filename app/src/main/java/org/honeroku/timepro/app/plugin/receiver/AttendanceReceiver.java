package org.honeroku.timepro.app.plugin.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.squareup.otto.Subscribe;

import org.honeroku.timepro.app.plugin.bundle.BundleManager;
import org.honeroku.timepro.app.plugin.bundle.BundleScrubber;
import org.honeroku.timepro.domain.entity.Account;
import org.honeroku.timepro.domain.repository.AccountRepository;
import org.honeroku.timepro.domain.service.TimeProService;
import org.honeroku.timepro.domain.event.ClockInEvent;
import org.honeroku.timepro.domain.event.ClockOutEvent;
import org.honeroku.timepro.util.EventBus;


public final class AttendanceReceiver extends BroadcastReceiver {

    private static final String TAG = AttendanceReceiver.class.getName();

    private Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (!com.twofortyfouram.locale.Intent.ACTION_FIRE_SETTING.equals(intent.getAction())) {
            Log.d(TAG, intent.getAction());
            return;
        }

        BundleScrubber.scrub(intent);

        Bundle bundle = intent.getBundleExtra(com.twofortyfouram.locale.Intent.EXTRA_BUNDLE);
        BundleScrubber.scrub(bundle);

        if (!BundleManager.isBundleValid(bundle)) {
            Log.d(TAG, "invalid bundle");
            return;
        }

        Account account = AccountRepository.load(context);
        if (account == null) {
            Toast.makeText(context, "アカウントが未設定です", Toast.LENGTH_SHORT).show();
            return;
        }

        EventBus.getInstance().register(this);
        this.context = context;
        boolean isClockIn = bundle.getBoolean(BundleManager.BUNDLE_EXTRA_CLOCK_IN_OUT);
        if (isClockIn) {
            TimeProService.clockIn(account);
        } else {
            TimeProService.clockOut(account);
        }
    }

    @Subscribe
    public void onClockInEvent(ClockInEvent event) {
        if (event.isSuccess()) {
            Toast.makeText(context, "出勤記録をつけました", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, event.getMessage(), Toast.LENGTH_LONG).show();
        }
        EventBus.getInstance().unregister(this);
    }

    @Subscribe
    public void onClockOutEvent(ClockOutEvent event) {
        if (event.isSuccess()) {
            Toast.makeText(context, "退勤記録をつけました", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, event.getMessage(), Toast.LENGTH_LONG).show();
        }
        EventBus.getInstance().unregister(this);
    }

}
