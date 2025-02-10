package com.scheduler.schedulerapp.service;

import com.scheduler.schedulerapp.model.Role;
import com.scheduler.schedulerapp.model.User;
import com.scheduler.schedulerapp.model.Person;
import com.scheduler.schedulerapp.model.Shift;
import com.scheduler.schedulerapp.repository.RoleRepository;
import com.scheduler.schedulerapp.repository.UserRepository;
import com.scheduler.schedulerapp.repository.PersonRepository;
import com.scheduler.schedulerapp.repository.ShiftRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.Set;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PersonRepository personRepository;
    private final ShiftRepository shiftRepository;
    private final PasswordEncoder passwordEncoder;

    public DatabaseSeeder(UserRepository userRepository,
                          RoleRepository roleRepository,
                          PersonRepository personRepository,
                          ShiftRepository shiftRepository,
                          PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.personRepository = personRepository;
        this.shiftRepository = shiftRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        // Ensure roles exist
        Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                .orElseGet(() -> roleRepository.save(new Role("ROLE_ADMIN")));
        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseGet(() -> roleRepository.save(new Role("ROLE_USER")));

        // Ensure admin user exists
        if (userRepository.findByUsername("admin").isEmpty()) {
            User adminUser = new User("admin", passwordEncoder.encode("admin123"));
            adminUser.setRoles(Set.of(adminRole));
            userRepository.save(adminUser);
        }

        // Ensure test user exists
        if (userRepository.findByUsername("testuser").isEmpty()) {
            User testUser = new User("testuser", passwordEncoder.encode("test123"));
            testUser.setRoles(Set.of(userRole));
            userRepository.save(testUser);
            System.out.println("Test user created: testuser/test123");
        }

        if (personRepository.count() == 0) {
            Person person1 = new Person();
            person1.setFirstName("Alice");
            person1.setLastName("Smith");

            Person person2 = new Person();
            person2.setFirstName("Bob");
            person2.setLastName("Johnson");

            Person person3 = new Person();
            person3.setFirstName("Charlie");
            person3.setLastName("Williams");

            Person person4 = new Person();
            person4.setFirstName("Diana");
            person4.setLastName("Brown");

            Person person5 = new Person();
            person5.setFirstName("Ethan");
            person5.setLastName("Davis");

            personRepository.saveAll(Arrays.asList(person1, person2, person3, person4, person5));
        }

        // Seed Shifts if none exist
        if (shiftRepository.count() == 0) {
            // 2 Morning Shifts (start 07:00)
            Shift morning1 = new Shift();
            morning1.setName("Morning Shift 1");
            morning1.setStartTime(LocalTime.of(7, 0));

            Shift morning2 = new Shift();
            morning2.setName("Morning Shift 2");
            morning2.setStartTime(LocalTime.of(7, 0));

            // 2 Evening Shifts (start 15:00)
            Shift evening1 = new Shift();
            evening1.setName("Evening Shift 1");
            evening1.setStartTime(LocalTime.of(15, 0));

            Shift evening2 = new Shift();
            evening2.setName("Evening Shift 2");
            evening2.setStartTime(LocalTime.of(15, 0));

            // 1 Night Shift (start 23:00)
            Shift night = new Shift();
            night.setName("Night Shift");
            night.setStartTime(LocalTime.of(23, 0));

            // 1 Day Shift (start 10:00) with an appropriate name
            Shift dayShift = new Shift();
            dayShift.setName("Day Shift");
            dayShift.setStartTime(LocalTime.of(10, 0));

            shiftRepository.saveAll(Arrays.asList(morning1, morning2, evening1, evening2, night, dayShift));
        }
    }
}
