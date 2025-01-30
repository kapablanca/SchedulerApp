package com.scheduler.schedulerapp.service;

import com.scheduler.schedulerapp.model.Role;
import com.scheduler.schedulerapp.model.User;
import com.scheduler.schedulerapp.repository.RoleRepository;
import com.scheduler.schedulerapp.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.util.Set;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public DatabaseSeeder(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        // Ensure roles exist
        Role adminRole = roleRepository.findByName("ROLE_ADMIN").orElseGet(() -> roleRepository.save(new Role("ROLE_ADMIN")));
        Role userRole = roleRepository.findByName("ROLE_USER").orElseGet(() -> roleRepository.save(new Role("ROLE_USER")));

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
    }
}



