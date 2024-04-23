package org.ldms.app.db;

import org.ldms.app.model.Installment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InstallmentRepo extends JpaRepository<Installment, Integer> {
    List<Installment> findAllByScheduleId(Integer id);

}
