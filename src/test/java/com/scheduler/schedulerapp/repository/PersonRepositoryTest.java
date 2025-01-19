package com.scheduler.schedulerapp.repository;

import com.scheduler.schedulerapp.model.Person;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class PersonRepositoryTest {

    @Autowired
    private PersonRepository personRepository;

    @Test
    public void testSaveAndFindAll() {

        Person person = new Person();
        person.setFirstName("John");
        person.setLastName("Doe");

        personRepository.save(person);

        List<Person> persons = personRepository.findAll();
        assertThat(persons).hasSize(1);
        assertThat(persons.get(0).getFirstName()).isEqualTo("John");
        assertThat(persons.get(0).getLastName()).isEqualTo("Doe");
    }

}
