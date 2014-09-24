package org.honeroku.timepro.domain.event;

import org.honeroku.timepro.domain.entity.Account;

public class LoginEvent extends Event {

    private Account account;

    public static LoginEvent createAsSuccess(Account account) {
        LoginEvent event = new LoginEvent();
        event.setResult(Result.SUCCESS);
        event.setAccount(account);
        return event;
    }

    public static LoginEvent createAsFailed(String message) {
        LoginEvent event = new LoginEvent();
        event.setResult(Result.FAILED);
        event.setMessage(message);
        return event;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

}
