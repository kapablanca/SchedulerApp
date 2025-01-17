package com.scheduler.schedulerapp.repository;

import com.scheduler.schedulerapp.model.Shift;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShiftRepository extends JpaRepository<Shift, Integer> {
}
