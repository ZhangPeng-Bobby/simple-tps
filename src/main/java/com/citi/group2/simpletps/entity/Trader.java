package com.citi.group2.simpletps.entity;

public class Trader {
    private Integer tId;

    private String tName;

    private String tAccount;

    private String tPwd;

    public Integer gettId() {
        return tId;
    }

    public void settId(Integer tId) {
        this.tId = tId;
    }

    public String gettName() {
        return tName;
    }

    public void settName(String tName) {
        this.tName = tName == null ? null : tName.trim();
    }

    public String gettAccount() {
        return tAccount;
    }

    public void settAccount(String tAccount) {
        this.tAccount = tAccount == null ? null : tAccount.trim();
    }

    public String gettPwd() {
        return tPwd;
    }

    public void settPwd(String tPwd) {
        this.tPwd = tPwd == null ? null : tPwd.trim();
    }
}