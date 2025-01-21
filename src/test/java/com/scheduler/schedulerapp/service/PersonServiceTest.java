package com.scheduler.schedulerapp.service;

import com.scheduler.schedulerapp.model.Person;
import com.scheduler.schedulerapp.repository.PersonRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;



public class PersonServiceTest {

    private PersonRepository personRepository;
    private PersonService personService;

    @BeforeEach
    void setUp() {
        personRepository = mock(PersonRepository.class); // Mock the repository
        personService = new PersonService(personRepository); // Inject the mock into the service
    }

    @Test
    void testSavePersonSuccess() {
        Person person = new Person();
        person.setFirstName("John");
        person.setLastName("Doe");

        when(personRepository.existsByFirstNameAndLastName("JOHN", "DOE")).thenReturn(false);
        when(personRepository.save(any(Person.class))).thenReturn(person);

        Person savedPerson = personService.savePerson(person);

        // Verify normalization and save call
        assertEquals("JOHN", savedPerson.getFirstName());
        assertEquals("DOE", savedPerson.getLastName());
        verify(personRepository, times(1)).save(person);
        verify(personRepository).existsByFirstNameAndLastName("JOHN", "DOE");

    }

    @Test
    void testSavePersonDuplicate() {
        Person person = new Person();
        person.setFirstName("John");
        person.setLastName("Doe");

        when(personRepository.existsByFirstNameAndLastName("JOHN", "DOE")).thenReturn(true);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> personService.savePerson(person));
        assertEquals("A person with the same first and last name already exists.", exception.getMessage());
    }

    @Test
    void testGetPersonsByFirstName() {
        Person person1 = new Person();
        person1.setFirstName("Alice");
        person1.setLastName("Smith");

        Person person2 = new Person();
        person2.setFirstName("Alice");
        person2.setLastName("Johnson");

        when(personRepository.findByFirstName("Alice")).thenReturn(Arrays.asList(person1, person2));

        List<Person> persons = personService.getPersonsByFirstName("Alice");

        assertEquals(2, persons.size());
        verify(personRepository, times(1)).findByFirstName("Alice");
    }

    @Test
    void testUpdatePerson() {
        Person existingPerson = new Person();
        existingPerson.setFirstName("Alice");
        existingPerson.setLastName("Smith");

        Person updatedPerson = new Person();
        updatedPerson.setFirstName("AliceUpdated");
        updatedPerson.setLastName("SmithUpdated");

        when(personRepository.findById(1L)).thenReturn(Optional.of(existingPerson));
        when(personRepository.save(any(Person.class))).thenReturn(existingPerson);

        Person result = personService.updatePerson(1L, updatedPerson);

        assertEquals("ALICEUPDATED", result.getFirstName());
        assertEquals("SMITHUPDATED", result.getLastName());
        verify(personRepository, times(1)).save(existingPerson);
    }
}
