package com.example.demo;

import com.example.demo.config.AppConstants;
import com.example.demo.controllers.CommentController;
import com.example.demo.entity.Role;
import com.example.demo.repositries.RoleRepo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class ExporaAppApplication implements CommandLineRunner {

    private final DaoAuthenticationProvider authenticationProvider;
    private final CommentController commentController;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepo roleRepo;

    @Autowired
    public ExporaAppApplication(CommentController commentController, DaoAuthenticationProvider authenticationProvider) {
        this.commentController = commentController;
        this.authenticationProvider = authenticationProvider;
    }

    public static void main(String[] args) {
        SpringApplication.run(ExporaAppApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        // Print encoded password for debugging
        System.out.println("Encoded password for 'satyam': " + this.passwordEncoder.encode("satyam"));

        try {
            List<Role> rolesToSave = new ArrayList<>();

            // ADMIN role
            Role roleAdmin = roleRepo.findById(AppConstants.ADMIN_USER)
                    .orElseGet(() -> {
                        Role r = new Role();
                        r.setName("ROLE_ADMIN");
                        return r;
                    });
            roleAdmin.setName("ROLE_NORMAL"); // Ensure name is updated
            rolesToSave.add(roleAdmin);

            // NORMAL USER role
            Role roleUser = roleRepo.findById(AppConstants.NORMAL_USER)
                    .orElseGet(() -> {
                        Role r = new Role();
                        r.setName("NORMAL_USER");
                        return r;
                    });
            roleUser.setName("NORMAL_USER"); // Ensure name is updated
            rolesToSave.add(roleUser);

            // Save roles safely
            List<Role> savedRoles = roleRepo.saveAll(rolesToSave);

            // Print saved roles
            savedRoles.forEach(r -> System.out.println("Role saved: ID=" + r.getId() + ", Name=" + r.getName()));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
