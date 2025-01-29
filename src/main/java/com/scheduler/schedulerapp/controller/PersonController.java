package com.scheduler.schedulerapp.controller;

import com.scheduler.schedulerapp.model.Person;
import com.scheduler.schedulerapp.service.PersonService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/people")
public class PersonController {

    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public String showPeopleList(Model model) {
        model.addAttribute("people", personService.getAllPersons());
        model.addAttribute("person", new Person()); // Initialize an empty person for the form
        return "people";
    }

    @GetMapping("/form")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String showPersonForm(@RequestParam(required = false) Long id, Model model) {
        Person person = (id != null) ? personService.getPersonById(id) : new Person();
        model.addAttribute("person", person); // Pass the person (either empty or existing) to the form
        return "people";
    }

    @PostMapping("/save")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String savePerson(@ModelAttribute Person person, Model model) {
        try {
            if (person.getId() != null) {
                // Update the existing person
                personService.updatePerson(person.getId(), person);
            } else {
                // Add a new person
                personService.savePerson(person);
            }
            return "redirect:/people";
        } catch (IllegalArgumentException e) {
            model.addAttribute("person", person);
            model.addAttribute("errorMessage", e.getMessage());
            return "people"; // Return to the form with the error message
        }
    }


    @GetMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String deletePerson(@PathVariable Long id) {
        personService.deletePersonById(id);
        return "redirect:/people";
    }
}
