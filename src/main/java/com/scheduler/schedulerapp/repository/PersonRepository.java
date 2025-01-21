package com.scheduler.schedulerapp.repository;

import com.scheduler.schedulerapp.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PersonRepository extends JpaRepository<Person, Long> {
    List<Person> findByLastName(String lastName);
    List<Person> findByFirstName(String firstName);
    List<Person> findByFirstNameAndLastName(String firstName, String lastName);
    boolean existsByFirstNameAndLastName(String firstName, String lastName);

}
