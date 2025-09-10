package com.example.demo.Security;

import com.example.demo.entity.User;
import com.example.demo.repositries.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * This service is responsible for loading user-specific data.
 * It implements Spring Security's UserDetailsService interface
 * which is used by Spring Security for authentication.
 */
@Service
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepo userRepo;

    @Autowired
    public CustomUserDetailService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    /**
     * Loads the user by their email. This method is automatically called by Spring Security during authentication.
     *
     * @param email the email (username) of the user trying to authenticate
     * @return UserDetails containing user data and authorities
     * @throws UsernameNotFoundException if the user is not found in the database
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Find the user by email using the repository
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        // Return the User object (which implements UserDetails)
        return user;
    }
}
