package org.honeroku.timepro.domain.service;

import android.os.AsyncTask;

import org.honeroku.timepro.domain.constraint.AccountConstraint;
import org.honeroku.timepro.domain.entity.Account;
import org.honeroku.timepro.event.ClockInEvent;
import org.honeroku.timepro.event.ClockOutEvent;
import org.honeroku.timepro.event.LoginEvent;
import org.honeroku.timepro.infrastructure.TimeProClient;

public class TimeProService {

    private static final String TAG = TimeProService.class.getName();

    public static void login(final Account account) {
        assert new AccountConstraint().isSatisfiedBy(account);

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                TimeProClient client = new TimeProClient(account.getDomain());
                if (client.validate(account.getUserId(), account.getPassword())) {
                    LoginEvent.createAsSuccess(account).publish();
                } else {
                    LoginEvent.createAsFailed(client.getErrorMessage()).publish();
                }
                return null;
            }
        }.execute();
    }

    public static void clockIn(final Account account) {
        assert new AccountConstraint().isSatisfiedBy(account);

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                TimeProClient client = new TimeProClient(account.getDomain());
                if (client.clockIn(account.getUserId(), account.getPassword())) {
                    ClockInEvent.createAsSuccess().publish();
                } else {
                    ClockInEvent.createAsFailed(client.getErrorMessage()).publish();
                }
                return null;
            }
        }.execute();
    }

    public static void clockOut(final Account account) {
        assert new AccountConstraint().isSatisfiedBy(account);

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                TimeProClient client = new TimeProClient(account.getDomain());
                if (client.clockOut(account.getUserId(), account.getPassword())) {
                    ClockOutEvent.createAsSuccess().publish();
                } else {
                    ClockOutEvent.createAsFailed(client.getErrorMessage()).publish();
                }
                return null;
            }
        }.execute();
    }

}
