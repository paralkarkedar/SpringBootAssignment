package com.example.usermanagement.config;

import com.example.usermanagement.model.Manager;
import com.example.usermanagement.repository.ManagerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final ManagerRepository managerRepository;

    @Override
    public void run(String... args) {
        if (managerRepository.count() == 0) {
            // Create sample managers
            List<Manager> managers = Arrays.asList(
                    new Manager(
                            UUID.fromString("a1b2c3d4-e5f6-7890-a1b2-c3d4e5f67890"),
                            "John Doe",
                            "Engineering",
                            LocalDateTime.now(),
                            true
                    ),
                    new Manager(
                            UUID.fromString("b2c3d4e5-f6a1-7890-b2c3-d4e5f6a17890"),
                            "Jane Smith",
                            "Marketing",
                            LocalDateTime.now(),
                            true
                    ),
                    new Manager(
                            UUID.fromString("c3d4e5f6-a1b2-7890-c3d4-e5f6a1b27890"),
                            "Bob Johnson",
                            "Finance",
                            LocalDateTime.now(),
                            true
                    )
            );

            managerRepository.saveAll(managers);
            log.info("Sample managers created successfully");
        }
    }
}