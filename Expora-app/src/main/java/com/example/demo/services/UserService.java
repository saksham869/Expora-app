package com.example.demo.services;

import java.util.List;
import com.example.demo.payloads.UserDto;

/**
 * Service interface for user-related operations.
 */
public interface UserService {
	
	UserDto registerNewUser(UserDto user);

    /** Creates a new user. */
    UserDto createUser(UserDto user);

    /** Updates an existing user. */
    UserDto updateUser(UserDto user, Integer userId);

    /** Gets a user by their ID. */
    UserDto getUserById(Integer userId);

    /** Gets all users. */
    List<UserDto> getAllUsers();

    /** Deletes a user by ID. */
    void deleteUser(Integer userId);

    // --- 🔥 Added Advanced Features below ---

    /** Fetches a user by their email address. */
    UserDto getUserByEmail(String email);

    /** Checks if a user exists by email address. */
    boolean existsByEmail(String email);

    /** Changes password for a user. */
    void changePassword(Integer userId, String oldPassword, String newPassword);

    /** Initiates forgot password process (send reset link or OTP). */
    void forgotPassword(String email);

    /** Assigns a new role to a user (e.g., ADMIN, USER, MODERATOR). */
    void assignRole(Integer userId, String role);

    /** Removes a role from a user. */
    void removeRole(Integer userId, String role);

    /** Deactivates (bans) a user temporarily. */
    void deactivateUser(Integer userId);

    /** Reactivates a previously deactivated (banned) user. */
    void activateUser(Integer userId);

    /** Searches users by a keyword (name, email, bio, etc.). */
    List<UserDto> searchUsers(String keyword);

    /** Fetches all users who belong to a specific role. */
    List<UserDto> getUsersByRole(String role);

}
