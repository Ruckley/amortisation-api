package org.ldms.app.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Objects;
@Entity(name = "installments")
@Table
public class Installment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer  id;
    @Column("schedule_id")
    private Integer  scheduleId;

    @Column
    private Integer period;
    @Column
    private Money payment;

    @Column
    private Money principle;

    @Column
    private Money interest;

    @Column
    private Money balance;

    /**
     * For spring data relational
     */
    public Installment() {}

    public Installment(Integer period, Money payment, Money principle, Money interest, Money balance) {
        this.period = period;
        this.payment = payment;
        this.principle = principle;
        this.interest = interest;
        this.balance = balance;
    }

    public Installment(Integer  scheduleId, Integer period, Money payment, Money principle, Money interest, Money balance) {
        this.scheduleId = scheduleId;
        this.period = period;
        this.payment = payment;
        this.principle = principle;
        this.interest = interest;
        this.balance = balance;
    }

    public Installment(Integer  id, Integer  scheduleId, Integer period, Money payment, Money principle, Money interest, Money balance) {
        this.id = id;
        this.scheduleId = scheduleId;
        this.period = period;
        this.payment = payment;
        this.principle = principle;
        this.interest = interest;
        this.balance = balance;
    }

    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }

    public Integer  getId() {
        return id;
    }

    public void setId(Integer  id) {
        this.id = id;
    }

    public Integer  getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(Integer  scheduleId) {
        this.scheduleId = scheduleId;
    }

    public Money getPayment() {
        return payment;
    }

    public void setPayment(Money payment) {
        this.payment = payment;
    }

    public Money getPrinciple() {
        return principle;
    }

    public void setPrinciple(Money principle) {
        this.principle = principle;
    }

    public Money getInterest() {
        return interest;
    }

    public void setInterest(Money interest) {
        this.interest = interest;
    }

    public Money getBalance() {
        return balance;
    }

    public void setBalance(Money balance) {
        this.balance = balance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Installment that = (Installment) o;
        return Objects.equals(scheduleId, that.scheduleId) && Objects.equals(period, that.period) && Objects.equals(payment, that.payment) && Objects.equals(principle, that.principle) && Objects.equals(interest, that.interest) && Objects.equals(balance, that.balance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, scheduleId, period, payment, principle, interest, balance);
    }

    @Override
    public String toString() {
        return "AmortisationInstallment{" +
                "id=" + id +
                ", scheduleId=" + scheduleId +
                ", period=" + period +
                ", payment=" + payment +
                ", principle=" + principle +
                ", interest=" + interest +
                ", balance=" + balance +
                '}';
    }
}
