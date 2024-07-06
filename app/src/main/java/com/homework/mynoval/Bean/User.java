package com.homework.mynoval.Bean;

public class User {

    private String account, password;

    public User(String account, String password) {
        this.account = account;
        this.password = password;
    }

    public User() {
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }
}
