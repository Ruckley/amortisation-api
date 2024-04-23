package org.ldms.app.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class ScheduleRequest {

    private String name;

    private Money assetValue;

    private Money deposit;

    private BigDecimal interestRate;

    private Integer numberPayments;

    private Money balloonPayment;
    private String info;

    public ScheduleRequest(
            String name,
            @JsonProperty(required = true) Money assetValue,
            @JsonProperty(required = true) Money deposit,
            @JsonProperty(required = true) BigDecimal interestRate,
            @JsonProperty(required = true) Integer numberPayments,
            @JsonProperty(required = true) Money balloonPayment,
            String info
    ) {
        this.name = name;
        this.assetValue = assetValue;
        this.deposit = deposit;
        this.interestRate = interestRate;
        this.numberPayments = numberPayments;
        this.balloonPayment = balloonPayment;
        this.info = info;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Money getAssetValue() {
        return assetValue;
    }

    public void setAssetValue(Money assetValue) {
        this.assetValue = assetValue;
    }

    public Money getDeposit() {
        return deposit;
    }

    public void setDeposit(Money deposit) {
        this.deposit = deposit;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }

    public Integer getNumberPayments() {
        return numberPayments;
    }

    public void setNumberPayments(Integer numberPayments) {
        this.numberPayments = numberPayments;
    }

    public Money getBalloonPayment() {
        return balloonPayment;
    }

    public void setBalloonPayment(Money balloonPayment) {
        this.balloonPayment = balloonPayment;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    @Override
    public String toString() {
        return "ScheduleRequest{" +
                "name='" + name + '\'' +
                ", assetValue=" + assetValue +
                ", deposit=" + deposit +
                ", interestRate=" + interestRate +
                ", numberPayments=" + numberPayments +
                ", balloonPayment=" + balloonPayment +
                ", info='" + info + '\'' +
                '}';
    }
}
