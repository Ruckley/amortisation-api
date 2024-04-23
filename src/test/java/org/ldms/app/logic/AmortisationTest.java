package org.ldms.app.logic;

import org.junit.jupiter.api.Test;
import org.ldms.app.TestObjects;
import org.ldms.app.model.Amortisation;
import org.ldms.app.model.Money;
import org.ldms.app.model.ScheduleRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class AmortisationTest {

    @Autowired
    AmortisationCalc amor;

    @Test
    void createsInstallmentsCorrectly() {

        TestObjects testObjects = new TestObjects();

        Amortisation result = amor.createAmortisation(testObjects.getScheduleRequest(), new BigInteger("12"));

        assertEquals(testObjects.getExpectedInstallments(), result.getInstallments());
        assertEquals(new Money("1735.15"), result.getSchedule().getPeriodPayment());
        assertEquals(new Money("821.79"), result.getSchedule().getTotalInterest());
        assertEquals(new Money("20821.79"), result.getSchedule().getTotalPayment());
    }

    @Test
    void createsInstallmentsWithBalloonCorrectly() {

        TestObjects testObjects = new TestObjects();

        Amortisation result = amor.createAmortisation(testObjects.getScheduleRequestWithBalloon(), new BigInteger("12"));

        assertEquals(testObjects.getBalloonInstallments(), result.getInstallments());
        assertEquals(new Money("930.07"), result.getSchedule().getPeriodPayment());
        assertEquals(new Money("1160.89"), result.getSchedule().getTotalInterest());
        assertEquals(new Money("21160.89"), result.getSchedule().getTotalPayment());
    }


    @Test
    void throwsExceptionWhenDepositMoreThanAssetValue() {
        ScheduleRequest request = new ScheduleRequest("name", new Money("25000"), new Money("30000"), new BigDecimal("0.075"), 12, new Money("0"), "info");
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> {
            amor.createAmortisation(request, new BigInteger("12"));
        });
        assertEquals(e.getMessage(), "deposit cannot be greater than assetValue");
    }

    @Test
    void throwsExceptionWhenInterestIsTooLowForAccuracy() {
        ScheduleRequest request = new ScheduleRequest("name", new Money("25000"), new Money("5000"), new BigDecimal("0.00000000075"), 12, new Money("0"), "info");
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> {
            amor.createAmortisation(request, new BigInteger("12"));
        });
        assertEquals(e.getMessage(), "periodic interest rate too low for accuracy of 6 decimal places");
    }

    @Test
    void throwsExceptionWhenNumberOfPaymentsBellow2() {
        ScheduleRequest request = new ScheduleRequest("name", new Money("25000"), new Money("5000"), new BigDecimal("0.075"), 1, new Money("0"), "info");
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> {
            amor.createAmortisation(request, new BigInteger("12"));
        });
        assertEquals(e.getMessage(), "Amortisations must have at least 2 installments");
    }

    @Test
    void throwsExceptionWhenBalloonIsNegative() {
        ScheduleRequest request = new ScheduleRequest("name", new Money("25000"), new Money("5000"), new BigDecimal("0.075"), 12, new Money("-1"), "info");
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> {
            amor.createAmortisation(request, new BigInteger("12"));
        });
        assertEquals(e.getMessage(), "Balloon Payment cannot be negative");
    }
}
