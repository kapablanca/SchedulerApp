package com.scheduler.schedulerapp.service;

import com.scheduler.schedulerapp.model.Shift;
import com.scheduler.schedulerapp.repository.ShiftRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ShiftServiceTest {
    private ShiftRepository shiftRepository;
    private ShiftService shiftService;

    @BeforeEach
    void setUp() {
        shiftRepository = mock(ShiftRepository.class);
        shiftService = new ShiftService(shiftRepository);
    }

    @Test
    void testSaveShiftSuccess() {
        Shift shift = new Shift();
        shift.setName("Morning");
        shift.setStartTime(LocalTime.of(8, 0));

        when(shiftRepository.existsByName("MORNING")).thenReturn(false);
        when(shiftRepository.save(any(Shift.class))).thenReturn(shift);

        Shift savedShift = shiftService.saveShift(shift);

        assertEquals("MORNING", savedShift.getName());
        assertEquals(LocalTime.of(8, 0), savedShift.getStartTime());
        verify(shiftRepository, times(1)).save(shift);
        verify(shiftRepository).existsByName("MORNING");
    }

    @Test
    void testSaveShiftDuplicate() {
        Shift shift = new Shift();
        shift.setName("Morning");
        shift.setStartTime(LocalTime.of(8, 0));

        when(shiftRepository.existsByName("MORNING")).thenReturn(true);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> shiftService.saveShift(shift));

        assertEquals("A shift with the same name already exists.", exception.getMessage());

        verify(shiftRepository, times(0)).save(any(Shift.class));
        verify(shiftRepository).existsByName("MORNING");
    }

    @Test
    void testGetShiftsByName() {
        Shift shift1 = new Shift();
        shift1.setName("Morning");

        Shift shift2 = new Shift();
        shift2.setName("Morning");

        when(shiftRepository.findByName("Morning")).thenReturn(Arrays.asList(shift1, shift2));

        List<Shift> shifts = shiftService.getShiftsByName("Morning");

        assertEquals(2, shifts.size());
        verify(shiftRepository, times(1)).findByName("Morning");
    }

    @Test
    void testUpdateShift() {
        Shift existingShift = new Shift();
        existingShift.setName("Morning");
        existingShift.setStartTime(LocalTime.of(8, 0));
        ReflectionTestUtils.setField(existingShift, "id", 1L); // Simulate ID assignment

        Shift updatedShift = new Shift();
        updatedShift.setName("Evening");
        updatedShift.setStartTime(LocalTime.of(16, 0));

        when(shiftRepository.findById(1L)).thenReturn(Optional.of(existingShift));
        when(shiftRepository.save(any(Shift.class))).thenReturn(existingShift);

        Shift result = shiftService.updateShift(1L, updatedShift);

        assertEquals("EVENING", result.getName());
        assertEquals(LocalTime.of(16, 0), result.getStartTime());
        verify(shiftRepository, times(1)).findById(1L);
        verify(shiftRepository, times(1)).save(existingShift);
    }
}
