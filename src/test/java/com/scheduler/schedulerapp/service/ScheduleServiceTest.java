package com.scheduler.schedulerapp.service;

import com.scheduler.schedulerapp.model.Person;
import com.scheduler.schedulerapp.model.Schedule;
import com.scheduler.schedulerapp.model.Shift;
import com.scheduler.schedulerapp.repository.ScheduleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ScheduleServiceTest {

    private ScheduleRepository scheduleRepository;
    private ScheduleService scheduleService;

    @BeforeEach
    void setUp() {
        scheduleRepository = mock(ScheduleRepository.class);
        scheduleService = new ScheduleService(scheduleRepository);
    }

    @Test
    void testSaveScheduleSuccess() {
        Schedule schedule = new Schedule();
        Person person = new Person();
        ReflectionTestUtils.setField(person, "id", 1L);
        Shift shift = new Shift();
        ReflectionTestUtils.setField(shift, "id", 1L);

        schedule.setPerson(person);
        schedule.setShift(shift);
        schedule.setDate(LocalDate.of(2025, 1, 20));

        when(scheduleRepository.existsByPersonIdAndDate(1L, LocalDate.of(2025, 1, 20))).thenReturn(false);
        when(scheduleRepository.save(any(Schedule.class))).thenReturn(schedule);

        Schedule savedSchedule = scheduleService.saveSchedule(schedule);

        assertEquals(person, savedSchedule.getPerson());
        assertEquals(shift, savedSchedule.getShift());
        assertEquals(LocalDate.of(2025, 1, 20), savedSchedule.getDate());
        verify(scheduleRepository, times(1)).save(schedule);
    }

    @Test
    void testSaveScheduleDuplicate() {
        Schedule schedule = new Schedule();
        Person person = new Person();
        ReflectionTestUtils.setField(person, "id", 1L);
        schedule.setPerson(person);
        schedule.setDate(LocalDate.of(2025, 1, 20));

        // Mock repository behavior
        when(scheduleRepository.existsByPersonIdAndDate(1L, LocalDate.of(2025, 1, 20))).thenReturn(true);

        // Assert that the service throws an exception
        Exception exception = assertThrows(IllegalArgumentException.class, () -> scheduleService.saveSchedule(schedule));
        assertEquals("This person already has a schedule for the given date.", exception.getMessage());

        // Verify that the save method is not called
        verify(scheduleRepository, times(0)).save(any(Schedule.class));
    }

    @Test
    void testGetSchedulesByPerson() {
        Schedule schedule1 = new Schedule();
        Schedule schedule2 = new Schedule();
        Person person = new Person();
        ReflectionTestUtils.setField(person, "id", 1L); // Set the person's ID

        ReflectionTestUtils.setField(schedule1, "person", person);
        ReflectionTestUtils.setField(schedule2, "person", person);

        List<Schedule> mockSchedules = Arrays.asList(schedule1, schedule2);

        when(scheduleRepository.findByPersonId(1L)).thenReturn(mockSchedules);

        List<Schedule> schedules = scheduleService.getSchedulesByPerson(1L);

        assertEquals(2, schedules.size());
        assertSame(person, schedules.get(0).getPerson());
        assertSame(person, schedules.get(1).getPerson());

        verify(scheduleRepository, times(1)).findByPersonId(1L);
    }

    @Test
    void testGetSchedulesByDate() {

        Schedule schedule1 = new Schedule();
        Schedule schedule2 = new Schedule();

        ReflectionTestUtils.setField(schedule1, "date", LocalDate.of(2025, 1, 20));
        ReflectionTestUtils.setField(schedule2, "date", LocalDate.of(2025, 1, 20));

        List<Schedule> mockSchedules = Arrays.asList(schedule1, schedule2);

        when(scheduleRepository.findByDate(LocalDate.of(2025, 1, 20))).thenReturn(mockSchedules);

        List<Schedule> schedules = scheduleService.getSchedulesByDate(LocalDate.of(2025, 1, 20));

        assertEquals(2, schedules.size());
        assertEquals(LocalDate.of(2025, 1, 20), schedules.get(0).getDate());
        assertEquals(LocalDate.of(2025, 1, 20), schedules.get(1).getDate());

        verify(scheduleRepository, times(1)).findByDate(LocalDate.of(2025, 1, 20));
    }

    @Test
    void testUpdateSchedule() {
        Schedule existingSchedule = new Schedule();
        Person oldPerson = new Person();
        Shift oldShift = new Shift();
        ReflectionTestUtils.setField(oldPerson, "id", 1L);
        ReflectionTestUtils.setField(oldShift, "id", 1L);
        ReflectionTestUtils.setField(existingSchedule, "id", 1L);
        ReflectionTestUtils.setField(existingSchedule, "person", oldPerson);
        ReflectionTestUtils.setField(existingSchedule, "shift", oldShift);
        ReflectionTestUtils.setField(existingSchedule, "date", LocalDate.of(2025, 1, 20));

        Schedule updatedSchedule = new Schedule();
        Person newPerson = new Person();
        Shift newShift = new Shift();
        ReflectionTestUtils.setField(newPerson, "id", 2L);
        ReflectionTestUtils.setField(newShift, "id", 2L);
        updatedSchedule.setPerson(newPerson);
        updatedSchedule.setShift(newShift);
        updatedSchedule.setDate(LocalDate.of(2025, 1, 21));

        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(existingSchedule));
        when(scheduleRepository.save(any(Schedule.class))).thenReturn(existingSchedule);

        Schedule result = scheduleService.updateSchedule(1L, updatedSchedule);

        assertEquals(newPerson, result.getPerson());
        assertEquals(newShift, result.getShift());
        assertEquals(LocalDate.of(2025, 1, 21), result.getDate());

        verify(scheduleRepository, times(1)).findById(1L);
        verify(scheduleRepository, times(1)).save(existingSchedule);
    }







}
