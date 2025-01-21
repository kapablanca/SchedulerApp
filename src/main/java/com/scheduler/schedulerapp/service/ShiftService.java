package com.scheduler.schedulerapp.service;

import com.scheduler.schedulerapp.model.Shift;
import com.scheduler.schedulerapp.repository.ShiftRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShiftService {

    private final ShiftRepository shiftRepository;

    public ShiftService(ShiftRepository shiftRepository) {
        this.shiftRepository = shiftRepository;
    }

    public List<Shift> getAllShifts() {
        return shiftRepository.findAll();
    }

    public Shift saveShift(Shift shift) {
        if (shift.getStartTime() == null) {
            throw new IllegalArgumentException("Start time is required.");
        }

        if (shift.getName() == null || shift.getName().isEmpty()) {
            throw new IllegalArgumentException("Shift name cannot be null or empty.");
        }

        if (shiftRepository.existsByName(shift.getName().toUpperCase())) {
            throw new IllegalArgumentException("A shift with the same name already exists.");
        }

        shift.setName(shift.getName().toUpperCase());

        return shiftRepository.save(shift);
    }

    public Shift updateShift(Long id, Shift updatedShift) {
        Shift existingShift = shiftRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Shift not found with id: " + id));

        if (updatedShift.getName() != null && !updatedShift.getName().isEmpty()) {
            existingShift.setName(updatedShift.getName().toUpperCase());
        } else {
            throw new IllegalArgumentException("Shift name cannot be null or empty.");
        }

        if (updatedShift.getStartTime() != null) {
            existingShift.setStartTime(updatedShift.getStartTime());
        } else {
            throw new IllegalArgumentException("Start time cannot be null.");
        }

        return shiftRepository.save(existingShift);
    }

    public Shift getShiftById(Long id) {
        return shiftRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Shift not found with id: " + id));
    }

    public List<Shift>getShiftsByName(String name) {
        return shiftRepository.findByName(name);
    }

    public void deleteShift(Long id) {
        if (!shiftRepository.existsById(id)) {
            throw new RuntimeException("Shift not found with id: " + id);
        }

        shiftRepository.deleteById(id);
    }

}
