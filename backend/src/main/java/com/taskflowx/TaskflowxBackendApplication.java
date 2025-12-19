package com.taskflowx;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.taskflowx.enums.Role;
import com.taskflowx.model.User;
import com.taskflowx.repository.UserRepository;

@SpringBootApplication
public class TaskflowxBackendApplication {
@Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public static void main(String[] args) {
        SpringApplication.run(TaskflowxBackendApplication.class, args);
    }

    @Bean
    public CommandLineRunner initData() {
        return args -> {
            // Create default ADMIN user if not exists
            if (!userRepository.existsByEmail("admin1@gmail.com")) {
                User admin = new User();
                admin.setEmail("admin1@gmail.com");
                admin.setPassword(passwordEncoder.encode("Admin@123"));
                admin.setFullName("System Administrator");
                admin.setRole(Role.ADMIN);
                admin.setIsActive(true);
                userRepository.save(admin);
                System.out.println(">>> Default Admin user created");
                System.out.println("   Email: admin1@gmail.com");
                System.out.println("   Password: Admin@123");
            }

            // Create sample MANAGER user
            if (!userRepository.existsByEmail("manager1@gmail.com")) {
                User manager = new User();
                manager.setEmail("manager1@gmail.com");
                manager.setPassword(passwordEncoder.encode("Admin@123"));
                manager.setFullName("John Manager");
                manager.setRole(Role.MANAGER);
                manager.setIsActive(true);
                userRepository.save(manager);
                System.out.println(">>> Sample Manager user created");
                System.out.println("   Email: manager1@gmail.com");
                System.out.println("   Password: Admin@123");
            }

            // Create sample EMPLOYEE users
            if (!userRepository.existsByEmail("employee1@gmail.com")) {
                User employee1 = new User();
                employee1.setEmail("employee1@gmail.com");
                employee1.setPassword(passwordEncoder.encode("Admin@123"));
                employee1.setFullName("Alice Employee");
                employee1.setRole(Role.EMPLOYEE);
                employee1.setIsActive(true);
                userRepository.save(employee1);
                System.out.println(">>> Sample Employee 1 created");
                System.out.println("   Email: employee1@gmail.com");
                System.out.println("   Password: Admin@123");
            }

            if (!userRepository.existsByEmail("employee2@gmail.com")) {
                User employee2 = new User();
                employee2.setEmail("employee2@gmail.com");
                employee2.setPassword(passwordEncoder.encode("Admin@123"));
                employee2.setFullName("Bob Employee");
                employee2.setRole(Role.EMPLOYEE);
                employee2.setIsActive(true);
                userRepository.save(employee2);
                System.out.println(">>> Sample Employee 2 created");
                System.out.println("   Email: employee2@gmail.com");
                System.out.println("   Password: Admin@123");
            }

            System.out.println("\n >>> TaskFlowX Backend Application Started Successfully!");
            System.out.println(">>> Sample users are ready for testing");
        };
    }
}
