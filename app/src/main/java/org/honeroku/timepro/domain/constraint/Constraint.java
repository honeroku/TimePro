package org.honeroku.timepro.domain.constraint;

import java.util.ArrayList;
import java.util.List;

public abstract class Constraint<E> {

    public abstract List<Rule<E>> getRules();

    public boolean isSatisfiedBy(E subject) {
        return getViolatedRules(subject).isEmpty();
    }

    public List<Rule<E>> getViolatedRules(E subject) {
        List<Rule<E>> violatedRules = new ArrayList<Rule<E>>();
        for (Rule<E> rule : getRules()) {
            if (!rule.getValidator().isValid(subject)) {
                violatedRules.add(rule);
            }
        }
        return violatedRules;
    }

    public List<String> getErrorMessages(E subject) {
        List<String> errorMessages = new ArrayList<String>();
        for (Rule violatedRule : getViolatedRules(subject)) {
            errorMessages.add(violatedRule.getErrorMessage());
        }
        return errorMessages;
    }

    public static interface Rule<E> {
        public String getErrorMessage();
        public Validator<E> getValidator();

        interface Validator<E> {
            public boolean isValid(E subject);
        }
    }
}
