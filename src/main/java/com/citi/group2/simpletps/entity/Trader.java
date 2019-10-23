package com.citi.group2.simpletps.entity;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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
        String pswHash = pswToHash(tPwd);
        this.tPwd = pswHash == null ? null : pswHash.trim();
    }

    private String pswToHash(String psw){
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.update(psw.getBytes());
            byte[] src = digest.digest();
            StringBuilder stringBuilder = new StringBuilder();
            for (byte aSrc : src) {
                String s = Integer.toHexString(aSrc & 0xFF);
                if (s.length() < 2) {
                    stringBuilder.append('0');
                }
                stringBuilder.append(s);
            }
            return stringBuilder.toString();
        } catch (NoSuchAlgorithmException ignore) {
        }
        return null;

    }
}