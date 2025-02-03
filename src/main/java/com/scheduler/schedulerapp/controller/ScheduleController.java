package com.scheduler.schedulerapp.controller;

import com.scheduler.schedulerapp.dto.DaySchedule;
import com.scheduler.schedulerapp.model.Person;
import com.scheduler.schedulerapp.model.Schedule;
import com.scheduler.schedulerapp.model.Shift;
import com.scheduler.schedulerapp.service.ScheduleService;
import com.scheduler.schedulerapp.service.ShiftService;
import com.scheduler.schedulerapp.service.PersonService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.*;

@Controller
@RequestMapping("/schedule")
public class ScheduleController {

    private final ScheduleService scheduleService;
    private final ShiftService shiftService;
    private final PersonService personService;

    public ScheduleController(ScheduleService scheduleService, ShiftService shiftService, PersonService personService) {
        this.scheduleService = scheduleService;
        this.shiftService = shiftService;
        this.personService = personService;
    }

    @GetMapping("")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public String getSchedule(@RequestParam(value = "year", required = false) Integer year,
                              @RequestParam(value = "month", required = false) Integer month,
                              Model model) {
        // If not provided, use the current year and month
        LocalDate now = LocalDate.now();
        if (year == null) {
            year = now.getYear();
        }
        if (month == null) {
            month = now.getMonthValue();
        }

        // Calculate the month name (e.g., "February")
        String monthName = Month.of(month).getDisplayName(TextStyle.FULL, Locale.ENGLISH);

        // Retrieve shifts and sort them by start time then alphabetically by name
        List<Shift> shifts = shiftService.getAllShifts();
        shifts.sort(Comparator.comparing(Shift::getStartTime).thenComparing(Shift::getName));

        // Build the list of days for the month
        YearMonth yearMonth = YearMonth.of(year, month);
        int daysInMonth = yearMonth.lengthOfMonth();
        List<DaySchedule> daySchedules = new ArrayList<>();
        for (int d = 1; d <= daysInMonth; d++) {
            LocalDate date = LocalDate.of(year, month, d);
            // Get abbreviated day name (e.g., "Mon")
            String dayName = date.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
            // Retrieve assignments for the date (assume it returns a Map with key = shift id, value = Person)
            Map<Long, Person> assignments = getAssignmentsForDate(date);
            DaySchedule ds = new DaySchedule(d, dayName, date.toString(), assignments);
            daySchedules.add(ds);
        }

        // Calculate previous and next month values for navigation
        YearMonth currentYM = YearMonth.of(year, month);
        YearMonth previousYM = currentYM.minusMonths(1);
        YearMonth nextYM = currentYM.plusMonths(1);

        model.addAttribute("currentYear", year);
        model.addAttribute("currentMonth", month);
        model.addAttribute("monthName", monthName);
        model.addAttribute("days", daySchedules);
        model.addAttribute("shiftList", shifts);
        model.addAttribute("peopleList", personService.getAllPersons());

        model.addAttribute("previousYear", previousYM.getYear());
        model.addAttribute("previousMonth", previousYM.getMonthValue());
        model.addAttribute("nextYear", nextYM.getYear());
        model.addAttribute("nextMonth", nextYM.getMonthValue());

        return "schedule";
    }


        // Endpoint to update a schedule assignment (only accessible to ADMIN)
        @PostMapping("/update")
        public String updateSchedule(@RequestParam("date") String dateStr,
                @RequestParam("shiftId") Long shiftId,
                @RequestParam(value = "personId", required = false) Long personId) {
            LocalDate date = LocalDate.parse(dateStr);
            // Update the assignment: if personId is null or empty, remove assignment; otherwise, assign that person
            scheduleService.updateAssignment(date, shiftId, personId);
            YearMonth ym = YearMonth.from(date);
            return "redirect:/schedule?year=" + ym.getYear() + "&month=" + ym.getMonthValue();
        }

        private Map<Long, Person> getAssignmentsForDate(LocalDate date) {
            List<Schedule> schedules = scheduleService.getSchedulesByDate(date);
            Map<Long, Person> assignments = new HashMap<>();
            for (Schedule schedule : schedules) {
                // Assuming only one schedule per shift per date.
                assignments.put(schedule.getShift().getId(), schedule.getPerson());
            }
            return assignments;
    }



    }
