package com.scheduler.schedulerapp.repository;

import com.scheduler.schedulerapp.model.Shift;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class ShiftRepositoryTest {

    @Autowired
    private ShiftRepository shiftRepository;

    @Test
    public void testFindAll() {
        Shift shift = new Shift();
        shift.setName("Morning");
        shift.setStartTime(LocalTime.of(8,0));

        shiftRepository.save(shift);

        List<Shift> shifts = shiftRepository.findAll();
        assertThat(shifts).hasSize(1);
        assertThat(shifts.get(0).getName()).isEqualTo("Morning");
        assertThat(shifts.get(0).getStartTime()).isEqualTo(LocalTime.of(8,0));
        assertThat(shifts.get(0).getEndTime()).isEqualTo(LocalTime.of(16,0));
    }

}
