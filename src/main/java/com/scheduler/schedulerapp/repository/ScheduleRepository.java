package com.scheduler.schedulerapp.repository;

import com.scheduler.schedulerapp.model.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    // Existing methods
    List<Schedule> findByPersonId(Long personId);
    List<Schedule> findByDate(LocalDate date);
    List<Schedule> findByPersonIdAndDate(Long personId, LocalDate date);
    boolean existsByPersonIdAndDate(Long personId, LocalDate date);

    // New method for fetching schedules within a date range
    List<Schedule> findByDateBetween(LocalDate startDate, LocalDate endDate);
}

