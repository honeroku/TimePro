package org.honeroku.timepro.domain.constraint;

import org.honeroku.timepro.domain.entity.Account;

import java.util.Arrays;
import java.util.List;

public class AccountConstraint extends Constraint<Account> {

    public enum AccountRule implements Rule<Account> {
        HAS_TIMEPRO_URL("ホストが指定されていません。", new Validator<Account>() {
            @Override
            public boolean isValid(Account account) {
                return account.hasDomain();
            }
        }),
        HAS_USER_ID("個人コードが指定されていません。", new Validator<Account>() {
            @Override
            public boolean isValid(Account account) {
                return account.hasUserId();
            }
        }),
        HAS_PASSWORD("パスワードが指定されていません。", new Validator<Account>() {
            @Override
            public boolean isValid(Account account) {
                return account.hasPassword();
            }
        });

        private String errorMessage;
        private Validator<Account> validator;

        private AccountRule(String errorMessage, Validator<Account> validator) {
            this.errorMessage = errorMessage;
            this.validator = validator;
        }

        @Override
        public String getErrorMessage() {
            return errorMessage;
        }

        @Override
        public Validator<Account> getValidator() {
            return validator;
        }
    }

    @Override
    public List<Rule<Account>> getRules() {
        return Arrays.<Rule<Account>>asList(AccountRule.values());
    }

}
