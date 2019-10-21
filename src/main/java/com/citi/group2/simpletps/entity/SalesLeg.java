package com.citi.group2.simpletps.entity;

import java.math.BigDecimal;

public class SalesLeg extends SalesLegKey {
    private Integer interVNum;

    private String status;

    private String rejectCode;

    private Integer sId;

    private Integer cId;

    private String cusip;

    private BigDecimal price;

    private Integer notionalAmount;

    private Integer matchedTraderLeg;

    private String rejectReason;

    public Integer getInterVNum() {
        return interVNum;
    }

    public void setInterVNum(Integer interVNum) {
        this.interVNum = interVNum;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getRejectCode() {
        return rejectCode;
    }

    public void setRejectCode(String rejectCode) {
        this.rejectCode = rejectCode == null ? null : rejectCode.trim();
    }

    public Integer getsId() {
        return sId;
    }

    public void setsId(Integer sId) {
        this.sId = sId;
    }

    public Integer getcId() {
        return cId;
    }

    public void setcId(Integer cId) {
        this.cId = cId;
    }

    public String getCusip() {
        return cusip;
    }

    public void setCusip(String cusip) {
        this.cusip = cusip == null ? null : cusip.trim();
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getNotionalAmount() {
        return notionalAmount;
    }

    public void setNotionalAmount(Integer notionalAmount) {
        this.notionalAmount = notionalAmount;
    }

    public Integer getMatchedTraderLeg() {
        return matchedTraderLeg;
    }

    public void setMatchedTraderLeg(Integer matchedTraderLeg) {
        this.matchedTraderLeg = matchedTraderLeg;
    }

    public String getRejectReason() {
        return rejectReason;
    }

    public void setRejectReason(String rejectReason) {
        this.rejectReason = rejectReason == null ? null : rejectReason.trim();
    }
}