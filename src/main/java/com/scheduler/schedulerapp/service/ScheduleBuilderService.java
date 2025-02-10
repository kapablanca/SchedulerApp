package com.scheduler.schedulerapp.service;

import com.scheduler.schedulerapp.dto.DaySchedule;
import com.scheduler.schedulerapp.model.Person;
import com.scheduler.schedulerapp.model.Schedule;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.*;

@Service
public class ScheduleBuilderService {

    private final ScheduleService scheduleService;

    public ScheduleBuilderService(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    /**
     * Build a list of DaySchedule DTOs for the given year and month.
     */
    public List<DaySchedule> buildDaySchedules(int year, int month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        int daysInMonth = yearMonth.lengthOfMonth();
        List<DaySchedule> daySchedules = new ArrayList<>();

        for (int d = 1; d <= daysInMonth; d++) {
            LocalDate date = LocalDate.of(year, month, d);
            // Get abbreviated day name (e.g., "Mon")
            String dayName = date.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
            // Build assignments for this date
            Map<Long, Person> assignments = buildAssignmentsForDate(date);
            DaySchedule ds = new DaySchedule(d, dayName, date.toString(), assignments);
            daySchedules.add(ds);
        }
        return daySchedules;
    }

    /**
     * Build assignments map: key = shift id, value = assigned Person.
     */
    private Map<Long, Person> buildAssignmentsForDate(LocalDate date) {
        List<Schedule> schedules = scheduleService.getSchedulesByDate(date);
        Map<Long, Person> assignments = new HashMap<>();
        for (Schedule schedule : schedules) {
            assignments.put(schedule.getShift().getId(), schedule.getPerson());
        }
        return assignments;
    }

    public YearMonth getPreviousYearMonth(int year, int month) {
        return YearMonth.of(year, month).minusMonths(1);
    }

    public YearMonth getNextYearMonth(int year, int month) {
        return YearMonth.of(year, month).plusMonths(1);
    }
}

