package org.ldms.app.api;

import org.ldms.app.db.InstallmentRepo;
import org.ldms.app.db.ScheduleRepo;
import org.ldms.app.logic.AmortisationCalc;
import org.ldms.app.model.Amortisation;
import org.ldms.app.model.Installment;
import org.ldms.app.model.Schedule;
import org.ldms.app.model.ScheduleRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Service
public class AmortisationService {

    @Autowired
    AmortisationCalc amorCalc;

    @Autowired
    ScheduleRepo scheduleRepo;
    @Autowired
    InstallmentRepo installmentRepo;

    Schedule createSchedule(ScheduleRequest request) {
        Amortisation amortisation = amorCalc.createAmortisation(request, new BigInteger("12"));
        Schedule s = scheduleRepo.save(amortisation.getSchedule());

        for (Installment installment : amortisation.getInstallments()) {
            installment.setScheduleId(s.getId());
        }

        installmentRepo.saveAll(amortisation.getInstallments());
        return s;
    }

    List<Schedule> getSchedules() {
        return scheduleRepo.findAll();
    }

    Optional<Amortisation> getAmortisation(Integer id) {

        return scheduleRepo.findById(id).map(s -> {
                    List<Installment> installments = installmentRepo.findAllByScheduleId(s.getId());
                    return new Amortisation(s, installments);
                }
        );
    }


}
