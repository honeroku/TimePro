package org.honeroku.timepro.domain.factory;

import org.honeroku.timepro.domain.entity.Account;

public class AccountFactory {

    public static Account create(String domain, String userId, String password) {
        Account account = new Account(domain, userId);
        account.setPassword(password);

        return account;
    }

}
