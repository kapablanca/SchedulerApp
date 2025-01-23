package com.scheduler.schedulerapp.service;

import com.scheduler.schedulerapp.model.Schedule;
import com.scheduler.schedulerapp.repository.ScheduleRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    public ScheduleService(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    public List<Schedule> getAllSchedules() {
        return scheduleRepository.findAll();
    }

    public List<Schedule> getSchedulesByPerson(Long personId) {
        return scheduleRepository.findByPersonId(personId);
    }

    public List<Schedule> getSchedulesByDate(LocalDate date) {
        return scheduleRepository.findByDate(date);
    }

    public Schedule saveSchedule(Schedule schedule) {
        if (scheduleRepository.existsByPersonIdAndDate(schedule.getPerson().getId(), schedule.getDate())) {
            throw new IllegalArgumentException("This person already has a schedule for the given date.");
        }
        return scheduleRepository.save(schedule);
    }

    public Schedule updateSchedule(Long id, Schedule updatedSchedule) {
        Schedule existingSchedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Schedule not found with id: " + id));

        existingSchedule.setPerson(updatedSchedule.getPerson());
        existingSchedule.setShift(updatedSchedule.getShift());
        existingSchedule.setDate(updatedSchedule.getDate());

        return scheduleRepository.save(existingSchedule);
    }

    public void deleteScheduleById(Long id) {
        if (!scheduleRepository.existsById(id)) {
            throw new RuntimeException("Schedule not found with id: " + id);
        }
        scheduleRepository.deleteById(id);
    }

}
