package com.scheduler.schedulerapp.controller;

import com.scheduler.schedulerapp.model.Shift;
import com.scheduler.schedulerapp.service.ShiftService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/shifts")
@Tag(name = "Shifts", description = "Operations related to Shifts")
public class ShiftRestController {

    private final ShiftService shiftService;

    public ShiftRestController(ShiftService shiftService) {
        this.shiftService = shiftService;
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @Operation(summary = "Get a shift by ID", description = "Returns a shift by its ID")
    public ResponseEntity<Shift> getShiftById(@PathVariable Long id) {
        Shift shift = shiftService.getShiftById(id);
        return ResponseEntity.ok(shift);
    }
}
