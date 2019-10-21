package com.citi.group2.simpletps.entity;

public class SalesLegKey {
    private String interId;

    private Integer txnId;

    public String getInterId() {
        return interId;
    }

    public void setInterId(String interId) {
        this.interId = interId == null ? null : interId.trim();
    }

    public Integer getTxnId() {
        return txnId;
    }

    public void setTxnId(Integer txnId) {
        this.txnId = txnId;
    }
}