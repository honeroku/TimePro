package org.honeroku.timepro.domain.entity;

public class Account {

    private final String domain;
    private final String userId;
    private String password;

    public Account(String domain, String userId) {
        this.domain = domain;
        this.userId = userId;
    }

    public String getDomain() {
        return domain;
    }

    public boolean hasDomain() {
        return domain != null && !domain.isEmpty();
    }

    public String getUserId() {
        return userId;
    }

    public boolean hasUserId() {
        return userId != null && !userId.isEmpty();
    }

    public String getPassword() {
        return password;
    }

    public boolean hasPassword() {
        return password != null && !password.isEmpty();
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
