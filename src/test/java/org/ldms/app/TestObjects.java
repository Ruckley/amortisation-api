package org.ldms.app;

import org.ldms.app.model.Installment;
import org.ldms.app.model.Money;
import org.ldms.app.model.ScheduleRequest;

import java.math.BigDecimal;
import java.util.List;

final public class TestObjects {

    final private ScheduleRequest scheduleRequest = new ScheduleRequest("name", new Money("25000"), new Money("5000"), new BigDecimal("0.075"), 12, new Money("0"), "info");

    final private ScheduleRequest scheduleRequestWithBalloon = new ScheduleRequest("name", new Money("25000"), new Money("5000"), new BigDecimal("0.075"), 12, new Money("10000"), "info");


    final private List<Installment> expectedInstallments = List.of(
            new Installment(1, new Money("1735.15"), new Money("1610.15"), new Money("125.00"), new Money("18389.85")),
            new Installment(2, new Money("1735.15"), new Money("1620.21"), new Money("114.94"), new Money("16769.64")),
            new Installment(3, new Money("1735.15"), new Money("1630.34"), new Money("104.81"), new Money("15139.30")),
            new Installment(4, new Money("1735.15"), new Money("1640.53"), new Money("94.62"), new Money("13498.77")),
            new Installment(5, new Money("1735.15"), new Money("1650.78"), new Money("84.37"), new Money("11847.99")),
            new Installment(6, new Money("1735.15"), new Money("1661.10"), new Money("74.05"), new Money("10186.89")),
            new Installment(7, new Money("1735.15"), new Money("1671.48"), new Money("63.67"), new Money("8515.41")),
            new Installment(8, new Money("1735.15"), new Money("1681.93"), new Money("53.22"), new Money("6833.48")),
            new Installment(9, new Money("1735.15"), new Money("1692.44"), new Money("42.71"), new Money("5141.04")),
            new Installment(10, new Money("1735.15"), new Money("1703.02"), new Money("32.13"), new Money("3438.02")),
            new Installment(11, new Money("1735.15"), new Money("1713.66"), new Money("21.49"), new Money("1724.36")),
            new Installment(12, new Money("1735.14"), new Money("1724.36"), new Money("10.78"), new Money("0.00"))
    );

    final private List<Installment> balloonInstallments = List.of(
            new Installment(1, new Money("930.07"), new Money("805.07"), new Money("125.00"), new Money("19194.93")),
            new Installment(2, new Money("930.07"), new Money("810.10"), new Money("119.97"), new Money("18384.83")),
            new Installment(3, new Money("930.07"), new Money("815.16"), new Money("114.91"), new Money("17569.67")),
            new Installment(4, new Money("930.07"), new Money("820.26"), new Money("109.81"), new Money("16749.41")),
            new Installment(5, new Money("930.07"), new Money("825.39"), new Money("104.68"), new Money("15924.02")),
            new Installment(6, new Money("930.07"), new Money("830.54"), new Money("99.53"), new Money("15093.48")),
            new Installment(7, new Money("930.07"), new Money("835.74"), new Money("94.33"), new Money("14257.74")),
            new Installment(8, new Money("930.07"), new Money("840.96"), new Money("89.11"), new Money("13416.78")),
            new Installment(9, new Money("930.07"), new Money("846.22"), new Money("83.85"), new Money("12570.56")),
            new Installment(10, new Money("930.07"), new Money("851.50"), new Money("78.57"), new Money("11719.06")),
            new Installment(11, new Money("930.07"), new Money("856.83"), new Money("73.24"), new Money("10862.23")),
            new Installment(12, new Money("10930.12"), new Money("10862.23"), new Money("67.89"), new Money("10000.00"))
    );

    public ScheduleRequest getScheduleRequest() {
        return scheduleRequest;
    }

    public ScheduleRequest getScheduleRequestWithBalloon() {
        return scheduleRequestWithBalloon;
    }

    public List<Installment> getExpectedInstallments() {
        return expectedInstallments;
    }

    public List<Installment> getBalloonInstallments() {
        return balloonInstallments;
    }
}

