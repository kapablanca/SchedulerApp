package com.scheduler.schedulerapp.controller;

import com.scheduler.schedulerapp.dto.DaySchedule;
import com.scheduler.schedulerapp.model.Shift;
import com.scheduler.schedulerapp.service.PersonService;
import com.scheduler.schedulerapp.service.ScheduleBuilderService;
import com.scheduler.schedulerapp.service.ScheduleService;
import com.scheduler.schedulerapp.service.ShiftService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

@Controller
@RequestMapping("/schedule")
public class ScheduleController {

    private final ScheduleBuilderService scheduleBuilderService;
    private final ScheduleService scheduleService; // Inject ScheduleService for update logic.
    private final ShiftService shiftService;
    private final PersonService personService;

    public ScheduleController(ScheduleBuilderService scheduleBuilderService,
                              ScheduleService scheduleService,
                              ShiftService shiftService,
                              PersonService personService) {
        this.scheduleBuilderService = scheduleBuilderService;
        this.scheduleService = scheduleService;
        this.shiftService = shiftService;
        this.personService = personService;
    }

    @GetMapping("")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public String getSchedule(@RequestParam(value = "year", required = false) Integer year,
                              @RequestParam(value = "month", required = false) Integer month,
                              Model model) {
        // Use current year/month if not provided
        LocalDate now = LocalDate.now();
        if (year == null) {
            year = now.getYear();
        }
        if (month == null) {
            month = now.getMonthValue();
        }

        // Calculate the month name (e.g., "February")
        String monthName = Month.of(month).getDisplayName(TextStyle.FULL, Locale.ENGLISH);

        // Retrieve and sort shifts by start time then alphabetically by name
        List<Shift> shifts = shiftService.getAllShifts();
        shifts.sort(Comparator.comparing(Shift::getStartTime).thenComparing(Shift::getName));

        // Build the day schedules using the ScheduleBuilderService
        List<DaySchedule> daySchedules = scheduleBuilderService.buildDaySchedules(year, month);

        // Calculate previous and next month values for navigation
        YearMonth previousYM = scheduleBuilderService.getPreviousYearMonth(year, month);
        YearMonth nextYM = scheduleBuilderService.getNextYearMonth(year, month);

        // Add attributes to the model
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

    @PostMapping("/update")
    public String updateSchedule(@RequestParam("date") String dateStr,
                                 @RequestParam("shiftId") Long shiftId,
                                 @RequestParam(value = "personId", required = false) Long personId) {
        LocalDate date = LocalDate.parse(dateStr);
        // Call the updateAssignment method to update (or remove) the assignment
        scheduleService.updateAssignment(date, shiftId, personId);
        YearMonth ym = YearMonth.from(date);
        return "redirect:/schedule?year=" + ym.getYear() + "&month=" + ym.getMonthValue();
    }
}
