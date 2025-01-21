package com.scheduler.schedulerapp.service;

import com.scheduler.schedulerapp.model.Person;
import com.scheduler.schedulerapp.repository.PersonRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonService {

    private final PersonRepository personRepository;

    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public List<Person> getAllPersons() {
        return personRepository.findAll();
    }

    public Person savePerson(Person person) {

        if (personRepository.existsByFirstNameAndLastName(person.getFirstName().toUpperCase(), person.getLastName().toUpperCase())) {
            throw new IllegalArgumentException("A person with the same first and last name already exists.");
        }

        person.setFirstName(person.getFirstName().toUpperCase());
        person.setLastName(person.getLastName().toUpperCase());

        return personRepository.save(person);
    }

    public Person updatePerson(Long id,Person updatedperson) {
        Person existingPerson = personRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Person not found"));

        existingPerson.setFirstName(updatedperson.getFirstName().toUpperCase());
        existingPerson.setLastName(updatedperson.getLastName().toUpperCase());

        return personRepository.save(existingPerson);
    }

    public Person getPersonById(long id) {
        return personRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Person not found with id: " + id));
    }

    public List<Person> getPersonsByLastName(String name) {
        return personRepository.findByLastName(name);
    }

    public List<Person> getPersonsByFirstName(String name) {
        return personRepository.findByFirstName(name);
    }

    public List<Person> getPersonsByFirstNameAndLastName(String firstName, String lastName) {
        return personRepository.findByFirstNameAndLastName(firstName, lastName);
    }

    public void deletePersonById(long id) {
        if (!personRepository.existsById(id)) {
            throw new RuntimeException("Person not found with id: " + id);
        }
        personRepository.deleteById(id);
    }

}
