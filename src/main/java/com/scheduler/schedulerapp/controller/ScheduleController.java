package com.scheduler.schedulerapp.controller;

import com.scheduler.schedulerapp.model.Schedule;
import com.scheduler.schedulerapp.service.ScheduleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/schedules")
public class ScheduleController {

    private final ScheduleService scheduleService;

    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @GetMapping
    public ResponseEntity<List<Schedule>> getAllSchedules() {
        List<Schedule> schedules = scheduleService.getAllSchedules();
        return ResponseEntity.ok(schedules);
    }

    @GetMapping("/person/{personId}")
    public ResponseEntity<List<Schedule>> getScheduleByPerson(@PathVariable long personId) {
        List<Schedule> schedules = scheduleService.getSchedulesByPerson(personId);
        return ResponseEntity.ok(schedules);
    }

    @GetMapping("/date/{date}")
    public ResponseEntity<List<Schedule>> getScheduleByDate(@PathVariable String date) {
        LocalDate localDate = LocalDate.parse(date);
        List<Schedule> schedules = scheduleService.getSchedulesByDate(localDate);
        return ResponseEntity.ok(schedules);
    }

    @PostMapping
    public ResponseEntity<Schedule> createSchedule(@RequestBody Schedule schedule) {
        Schedule savedSchedule = scheduleService.saveSchedule(schedule);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedSchedule);
    }

    @PutMapping
    public ResponseEntity<Schedule> updateSchedule(@PathVariable long id, @RequestBody Schedule schedule) {
        Schedule updatedSchedule = scheduleService.updateSchedule(id, schedule);
        return ResponseEntity.ok(updatedSchedule);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Schedule> deleteSchedule(@PathVariable long id) {
        scheduleService.deleteScheduleById(id);
        return ResponseEntity.noContent().build();
    }


}
