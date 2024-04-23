package org.ldms.app.model;

import java.util.List;

public class Amortisation {

    private Schedule schedule;
    private List<Installment> installments;

    public Amortisation(Schedule schedule, List<Installment> installments) {
        this.schedule = schedule;
        this.installments = installments;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public List<Installment> getInstallments() {
        return installments;
    }

    public void setInstallments(List<Installment> installments) {
        this.installments = installments;
    }
}
