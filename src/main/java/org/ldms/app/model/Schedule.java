package org.ldms.app.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Objects;

@Entity(name = "schedules")
@Table
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer  id;

    @Column
    private String name;

    @Column("asset_value")
    private Money assetValue;

    @Column("deposit_amount")
    private Money depositAmount;

    @Column("balloon_payment")
    private Money balloonPayment;
    @Column
    private String info;
    @Column
    private Money periodPayment;
    @Column
    private Money totalInterest;
    @Column
    private Money totalPayment;

    public Schedule() {}

    public Schedule(String name, Money assetValue, Money depositAmount, Money balloonPayment, String info, Money periodPayment, Money totalInterest, Money totalPayment) {
        this.name = name;
        this.assetValue = assetValue;
        this.depositAmount = depositAmount;
        this.balloonPayment = balloonPayment;
        this.info = info;
        this.periodPayment = periodPayment;
        this.totalInterest = totalInterest;
        this.totalPayment = totalPayment;
    }

    public Schedule(Integer  id, String name, Money assetValue, Money depositAmount, Money balloonPayment, String info, Money periodPayment, Money totalInterest, Money totalPayment) {
        this.id = id;
        this.name = name;
        this.assetValue = assetValue;
        this.depositAmount = depositAmount;
        this.balloonPayment = balloonPayment;
        this.info = info;
        this.periodPayment = periodPayment;
        this.totalInterest = totalInterest;
        this.totalPayment = totalPayment;
    }

    public Schedule(Schedule other) {
        this.name = other.name;
        this.assetValue = other.assetValue;
        this.depositAmount = other.depositAmount;
        this.balloonPayment = other.balloonPayment;
        this.info = other.info;
        this.periodPayment = other.periodPayment;
        this.totalInterest = other.totalInterest;
        this.totalPayment = other.totalPayment;
    }

    public Integer  getId() {
        return id;
    }

    public void setId(Integer  id) {
        this.id = id;
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

    public Money getDepositAmount() {
        return depositAmount;
    }

    public void setDepositAmount(Money depositAmount) {
        this.depositAmount = depositAmount;
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

    public Money getPeriodPayment() {
        return periodPayment;
    }

    public void setPeriodPayment(Money periodPayment) {
        this.periodPayment = periodPayment;
    }

    public Money getTotalInterest() {
        return totalInterest;
    }

    public void setTotalInterest(Money totalInterest) {
        this.totalInterest = totalInterest;
    }

    public Money getTotalPayment() {
        return totalPayment;
    }

    public void setTotalPayment(Money totalPayment) {
        this.totalPayment = totalPayment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Schedule schedule = (Schedule) o;
        return Objects.equals(id, schedule.id) && Objects.equals(name, schedule.name) && Objects.equals(assetValue, schedule.assetValue) && Objects.equals(depositAmount, schedule.depositAmount) && Objects.equals(balloonPayment, schedule.balloonPayment) && Objects.equals(info, schedule.info) && Objects.equals(periodPayment, schedule.periodPayment) && Objects.equals(totalInterest, schedule.totalInterest) && Objects.equals(totalPayment, schedule.totalPayment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, assetValue, depositAmount, balloonPayment, info, periodPayment, totalInterest, totalPayment);
    }

    @Override
    public String toString() {
        return "Schedule{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", assetValue=" + assetValue +
                ", depositAmount=" + depositAmount +
                ", balloonPayment=" + balloonPayment +
                ", info='" + info + '\'' +
                ", periodPayment=" + periodPayment +
                ", totalInterest=" + totalInterest +
                ", totalPayment=" + totalPayment +
                '}';
    }
}
