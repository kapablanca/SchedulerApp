package com.scheduler.schedulerapp.controller;

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
import java.util.List;

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

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public String showMonthlySchedule(
            @RequestParam(value = "year", required = false) Integer year,
            @RequestParam(value = "month", required = false) Integer month,
            Model model) {

        LocalDate currentDate = (year == null || month == null)
                ? LocalDate.now()
                : LocalDate.of(year, month, 1);

        LocalDate firstDayOfMonth = currentDate.withDayOfMonth(1);
        LocalDate lastDayOfMonth = currentDate.withDayOfMonth(currentDate.lengthOfMonth());

        List<Schedule> monthlySchedules = scheduleService.getSchedulesByDateRange(firstDayOfMonth, lastDayOfMonth);

        model.addAttribute("schedules", monthlySchedules);
        model.addAttribute("currentMonth", currentDate.getMonthValue());
        model.addAttribute("currentYear", currentDate.getYear());
        model.addAttribute("daysInMonth", currentDate.lengthOfMonth());

        return "schedule";
    }

    @PostMapping("/generate")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String generateSchedule(@RequestParam String monthStart, Model model) {
        LocalDate startOfMonth = LocalDate.parse(monthStart);
        List<Shift> shifts = shiftService.getAllShifts();
        List<Person> people = personService.getAllPersons();

        scheduleService.generateMonthlySchedule(startOfMonth, shifts, people);
        model.addAttribute("message", "Schedule generated successfully for: " + startOfMonth);
        return "redirect:/schedule?year=" + startOfMonth.getYear() + "&month=" + startOfMonth.getMonthValue();
    }
}
