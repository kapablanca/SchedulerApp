package com.scheduler.schedulerapp.controller;

import com.scheduler.schedulerapp.model.Shift;
import com.scheduler.schedulerapp.service.ShiftService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/shifts")
public class ShiftController {

    private final ShiftService shiftService;

    public ShiftController(ShiftService shiftService) {
        this.shiftService = shiftService;
    }

    @GetMapping
    public ResponseEntity<List<Shift>> getAllShifts() {
        List<Shift> shifts = shiftService.getAllShifts();
        return ResponseEntity.ok(shifts);
    }

    @PostMapping
    public ResponseEntity<Shift> createShift(@RequestBody Shift shift) {
        Shift savedShift = shiftService.saveShift(shift);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedShift);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Shift> updateShift(@PathVariable Long id, @RequestBody Shift shift) {
        Shift updatedShift = shiftService.updateShift(id, shift);
        return ResponseEntity.ok(updatedShift);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShiftById(@PathVariable Long id) {
        shiftService.deleteShiftById(id);
        return ResponseEntity.noContent().build();
    }


}
