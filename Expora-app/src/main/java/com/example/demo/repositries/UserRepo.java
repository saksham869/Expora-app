// Package where this repository is located
package com.example.demo.repositries;

import java.util.List;
// Importing required classes
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Role;
import com.example.demo.entity.User;

// This interface is a repository layer for the User entity.
// It extends JpaRepository, which provides built-in CRUD operations like save(), findById(), delete(), etc.
public interface UserRepo extends JpaRepository<User, Integer> {

    /**
     * Custom query method to find a user by their email address.
     * Spring Data JPA automatically generates the query based on the method name.
     * 
     * Example usage:
     * Optional<User> user = userRepo.findByEmail("user@example.com");
     * 
     * @param email the email of the user you want to find
     * @return Optional containing the User if found, or empty if not
     */
    Optional<User> findByEmail(String email);

	List<User> findByRoles(Role role);
}
