package com.scheduler.schedulerapp.repository;

import com.scheduler.schedulerapp.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Long> {

}
