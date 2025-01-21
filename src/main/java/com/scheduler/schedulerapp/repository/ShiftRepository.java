package com.scheduler.schedulerapp.repository;

import com.scheduler.schedulerapp.model.Shift;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShiftRepository extends JpaRepository<Shift, Long> {
    List<Shift> findByName(String name);
    boolean existsByName(String name);
}
