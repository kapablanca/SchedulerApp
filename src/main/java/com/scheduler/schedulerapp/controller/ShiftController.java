package com.scheduler.schedulerapp.controller;

import com.scheduler.schedulerapp.model.Shift;
import com.scheduler.schedulerapp.service.ShiftService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/shifts")
public class ShiftController {

    private final ShiftService shiftService;

    public ShiftController(ShiftService shiftService) {
        this.shiftService = shiftService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public String getAllShifts(Model model) {
        model.addAttribute("shifts", shiftService.getAllShifts());
        return "shifts"; // The name of the Thymeleaf template
    }

    @PostMapping("/save")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String saveShift(@ModelAttribute Shift shift, Model model) {
        try {
            // Validate input fields
            if (shift.getStartTime() == null || shift.getName() == null || shift.getName().isBlank()) {
                model.addAttribute("shift", shift);
                model.addAttribute("errorMessage", "Shift name and start time are required.");
                return "shifts";  // Return to the form with validation error
            }

            // Automatically calculate end time
            shift.setStartTime(shift.getStartTime());

            // Save or update the shift
            if (shift.getId() != null) {
                shiftService.updateShift(shift.getId(), shift);
            } else {
                shiftService.saveShift(shift);
            }

            return "redirect:/shifts";  // Redirect after successful save
        } catch (IllegalArgumentException e) {
            model.addAttribute("shift", shift);
            model.addAttribute("errorMessage", e.getMessage());
            return "shifts";  // Return to the form with an error message
        }
    }

    @GetMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String deleteShift(@PathVariable Long id) {
        shiftService.deleteShiftById(id);
        return "redirect:/shifts";
    }
}
