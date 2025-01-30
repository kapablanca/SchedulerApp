package com.scheduler.schedulerapp.service;

import com.scheduler.schedulerapp.model.Person;
import com.scheduler.schedulerapp.model.Schedule;
import com.scheduler.schedulerapp.model.Shift;
import com.scheduler.schedulerapp.repository.ScheduleRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    public ScheduleService(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    public List<Schedule> getAllSchedules() {
        return scheduleRepository.findAll();
    }

    public List<Schedule> getSchedulesByPerson(Long personId) {
        return scheduleRepository.findByPersonId(personId);
    }

    public List<Schedule> getSchedulesByDate(LocalDate date) {
        return scheduleRepository.findByDate(date);
    }

    public List<Schedule> getSchedulesByDateRange(LocalDate startDate, LocalDate endDate) {
        return scheduleRepository.findByDateBetween(startDate, endDate);
    }

    public Schedule saveSchedule(Schedule schedule) {
        // Ensure no duplicate schedules are added for the same person and day
        if (scheduleRepository.existsByPersonIdAndDate(schedule.getPerson().getId(), schedule.getDate())) {
            throw new IllegalArgumentException("This person already has a schedule for the given date.");
        }
        return scheduleRepository.save(schedule);
    }

    public Schedule updateSchedule(Long id, Schedule updatedSchedule) {
        Schedule existingSchedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Schedule not found with id: " + id));
        existingSchedule.setPerson(updatedSchedule.getPerson());
        existingSchedule.setShift(updatedSchedule.getShift());
        existingSchedule.setDate(updatedSchedule.getDate());
        return scheduleRepository.save(existingSchedule);
    }

    public void deleteScheduleById(Long id) {
        if (!scheduleRepository.existsById(id)) {
            throw new RuntimeException("Schedule not found with id: " + id);
        }
        scheduleRepository.deleteById(id);
    }

    public List<Schedule> generateMonthlySchedule(LocalDate monthStart, List<Shift> shifts, List<Person> people) {
        List<Schedule> schedules = new ArrayList<>();

        LocalDate currentDate = monthStart.withDayOfMonth(1);
        LocalDate endOfMonth = monthStart.withDayOfMonth(monthStart.lengthOfMonth());

        int personIndex = 0;

        while (!currentDate.isAfter(endOfMonth)) {
            for (Shift shift : shifts) {
                Person assignedPerson = people.get(personIndex % people.size());
                Schedule schedule = new Schedule();
                schedule.setDate(currentDate);
                schedule.setShift(shift);
                schedule.setPerson(assignedPerson);

                schedules.add(schedule);
                personIndex++;
            }
            currentDate = currentDate.plusDays(1);
        }

        return scheduleRepository.saveAll(schedules);
    }

}

