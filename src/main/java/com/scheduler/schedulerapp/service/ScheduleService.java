package com.scheduler.schedulerapp.service;

import com.scheduler.schedulerapp.model.Person;
import com.scheduler.schedulerapp.model.Schedule;
import com.scheduler.schedulerapp.model.Shift;
import com.scheduler.schedulerapp.repository.ScheduleRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final PersonService personService;
    private final ShiftService shiftService;

    public ScheduleService(ScheduleRepository scheduleRepository,
                           PersonService personService,
                           ShiftService shiftService) {
        this.scheduleRepository = scheduleRepository;
        this.personService = personService;
        this.shiftService = shiftService;
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


    public void updateAssignment(LocalDate date, Long shiftId, Long personId) {
        // Retrieve schedules for the given date
        List<Schedule> schedules = scheduleRepository.findByDate(date);

        // Find the schedule corresponding to the given shiftId
        Optional<Schedule> optionalSchedule = schedules.stream()
                .filter(s -> s.getShift().getId().equals(shiftId))
                .findFirst();

        if (personId == null || personId == 0) {
            // If no person is selected, remove the assignment if it exists.
            if (optionalSchedule.isPresent()) {
                scheduleRepository.delete(optionalSchedule.get());
            }
        } else {
            // Otherwise, update or create the assignment.
            // You will need a method in your PersonService to fetch the Person by id:
            Person person = personService.getPersonById(personId);
            if (optionalSchedule.isPresent()) {
                Schedule schedule = optionalSchedule.get();
                schedule.setPerson(person);
                scheduleRepository.save(schedule);
            } else {
                // Optionally, create a new schedule if one doesn't exist for that shift on that day.
                Schedule schedule = new Schedule();
                schedule.setDate(date);
                // You will also need a method in your ShiftService to fetch the shift by id:
                Shift shift = shiftService.getShiftById(shiftId);
                schedule.setShift(shift);
                schedule.setPerson(person);
                scheduleRepository.save(schedule);
            }
        }
    }

}

