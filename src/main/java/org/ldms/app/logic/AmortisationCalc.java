package org.ldms.app.logic;

import org.ldms.app.model.*;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Component
public class AmortisationCalc {

    private final RoundingMode rm = RoundingMode.HALF_UP;
    private final Integer INTEREST_SCALE = 6;
    private final BigDecimal ONE = BigDecimal.ONE.setScale(INTEREST_SCALE, rm);
    private final BigDecimal MIN_PERIODIC_INTEREST = ONE.divide(BigDecimal.TEN.pow(INTEREST_SCALE), INTEREST_SCALE, rm);


    public Amortisation createAmortisation(ScheduleRequest request, BigInteger paymentsPerYear ) {

        if(request.getAssetValue().compareTo(Money.ZERO) < 0) throw new IllegalArgumentException("AssetValue cannot be negative");
        if(request.getDeposit().compareTo(Money.ZERO) < 0) throw new IllegalArgumentException("Deposit cannot be negative");

        Money totalBalance = request.getAssetValue().subtract(request.getDeposit());
        if(totalBalance.compareTo(Money.ZERO) < 0) throw new IllegalArgumentException("deposit cannot be greater than assetValue");

        Integer numPayments = request.getNumberPayments();
        if(numPayments < 2) throw new IllegalArgumentException("Amortisations must have at least 2 installments");

        if(paymentsPerYear.compareTo(BigInteger.ZERO) < 0) throw new IllegalArgumentException("Payments per year cannot be negative");
        BigDecimal periodInterest = request.getInterestRate().divide(new BigDecimal(paymentsPerYear), INTEREST_SCALE, rm);
        if (periodInterest.compareTo(MIN_PERIODIC_INTEREST) < 0) throw new IllegalArgumentException("periodic interest rate too low for accuracy of " + INTEREST_SCALE + " decimal places");

        Money balloonPayment = request.getBalloonPayment();
        if(balloonPayment == null) {balloonPayment = new Money(BigDecimal.ZERO);}
        else if(balloonPayment.compareTo(Money.ZERO) < 0) {throw new IllegalArgumentException("Balloon Payment cannot be negative");}

        return createAmortisation(totalBalance, balloonPayment, periodInterest, numPayments, request);

        }

    private Amortisation createAmortisation(Money totalBalance, Money balloonPayment, BigDecimal periodInterest, Integer numberPayments, ScheduleRequest request){

        Money periodPayment = (balloonPayment.equals(Money.ZERO)) ?
                calcRepayment(totalBalance.asBigDecimal().setScale(INTEREST_SCALE, rm), periodInterest, numberPayments) :
                calcRepaymentWithBalloon(totalBalance.asBigDecimal().setScale(INTEREST_SCALE, rm), periodInterest, balloonPayment.asBigDecimal(), numberPayments);

        List<Installment> installments = createInstallments(numberPayments, totalBalance, periodPayment, periodInterest, balloonPayment);

        Money totalInterest = Money.ZERO;
        Money totalPayment = Money.ZERO;
        for (Installment installment : installments) {
            totalInterest = totalInterest.add(installment.getInterest());
            totalPayment = totalPayment.add(installment.getPayment());
        }

        Schedule schedule =  new Schedule(request.getName(), request.getAssetValue(), request.getDeposit(), balloonPayment, request.getInfo(), periodPayment, totalInterest, totalPayment);

        return new Amortisation(schedule, installments);

    }

    /**
     * Calculates the periodic repayment from this equation:
     * p * ((r * (1 + r) ^ n) / ((1 + r) ^ n - 1)
     * All operations not using monetary values are calculated to 5dp
     *
     * @param p Amount to be financed
     * @param r Interest rate for periodicity
     * @param n Number of payments
     * @return Periodic repayment amount
     */
    private Money calcRepayment(BigDecimal p, BigDecimal r, Integer n) {
        BigDecimal rPlus1PowN = r.add(ONE).pow(n);
        BigDecimal rPlus1PowNMinus1 = rPlus1PowN.subtract(ONE);
        BigDecimal rPlus1PowNMulR = rPlus1PowN.multiply(r);

        return new Money(p.multiply(rPlus1PowNMulR.divide(rPlus1PowNMinus1, rm)));
    }

    /**
     * Calculates the periodic repayment with a balloon payment from this equation:
     * (p - (b / ((1 + r) ^ n))) * (r / (1 - (1 + r) ^ -n))
     * All operations not using monetary values are calculated to 5dp
     *
     * @param p Amount to be financed
     * @param r Interest rate for periodicity
     * @param b Value of balloon payment
     * @param n Number of payments
     * @return Periodic repayment amount
     */
    private Money calcRepaymentWithBalloon(BigDecimal p, BigDecimal r, BigDecimal b, Integer n) {
        BigDecimal rPlus1PowN = r.add(ONE).pow(n);
        BigDecimal rPlus1PowNegN = ONE.divide(rPlus1PowN, rm);
        // (p -(b / ((1 + r) ^ n))))
        BigDecimal x = p.subtract(b.divide(rPlus1PowN, rm));
        // (r / (1 - (1 + r) ^ -n))
        BigDecimal y = r.divide(ONE.subtract(rPlus1PowNegN), rm);

        return new Money(x.multiply(y));
    }


    /**
     * Creates the amortisation installments by iteratively calculating the interest and principle portion form the previous installment.
     * Accounts for any rounding errors causing the final balance to be plus or minus a small value by changing the final
     * repayment and principle value.
     *
     * @param numPayments       The number of repayments in the schedule.
     * @param totalBalance      The total amount to be repaid (assetValue - deposit)
     * @param periodPayment     The payment made each installment
     * @param periodInterest    The interest per installment period (annual interest / number of payments)
     * @param balloonPayment    The balloon payment. Set to 0 if there is no balloon payment
     * @return A vector of amortisation installments.
     */
    private List<Installment> createInstallments(Integer numPayments, Money totalBalance, Money periodPayment, BigDecimal periodInterest, Money balloonPayment){
        Money remainingBalance = totalBalance;
        List<Installment> installments = new ArrayList<>();

        for(Integer period = 1; period <= numPayments; period++){
            Money interestPortion = remainingBalance.multiply(periodInterest);
            Money principlePortion = periodPayment.subtract(interestPortion);
            Money newBalance = remainingBalance.subtract(principlePortion);

            // ensures the final installment accounts for rounding errors and takes balance to zero or to the balloon payment
            if (period.equals(numPayments)){
                periodPayment = periodPayment.add(newBalance);
                principlePortion = principlePortion.add(newBalance);
                newBalance = balloonPayment;
            }

            Installment installment = new Installment(period, periodPayment, principlePortion, interestPortion, newBalance);
            installments.add(installment);
            remainingBalance = newBalance;
        }

        return installments;
    }




}
