package org.ldms.app.api;

import org.ldms.app.model.Amortisation;
import org.ldms.app.model.Schedule;
import org.ldms.app.model.ScheduleRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@RestController
@RequestMapping("/amortisation")
public class Controller {

    @Autowired
    private AmortisationService amorService;

    @PostMapping("/create_schedule")
    public ResponseEntity<?> createSchedule(@RequestBody ScheduleRequest request) {
        Schedule result = amorService.createSchedule(request);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/get_schedules")
    public ResponseEntity<?> getSchedules() {
        List<Schedule> result = amorService.getSchedules();
        if (result.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No schedules in DB");
        else return ResponseEntity.ok(result);
    }

    @GetMapping("/get_schedule_info")
    public ResponseEntity<?> getScheduleInfo(@RequestParam(required = true, name = "id") Integer id) {
        Optional<Amortisation> result = amorService.getAmortisation(id);
        if (result.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Could not find schedule with ID: "  + id);
        else return ResponseEntity.ok(result.get());
    }


}
