package com.example.demo.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.example.demo.exceptions.ResourceNotFoundException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.config.AppConstants;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.payloads.UserDto;
import com.example.demo.repositries.RoleRepo;
import com.example.demo.repositries.UserRepo;
import com.example.demo.services.UserService;

@Service
public class UserServiceImpli implements UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepo roleRepo;

    // ✅ CREATE USER
    @Override
    public UserDto createUser(UserDto userDto) {
        User user = this.modelMapper.map(userDto, User.class);
        User savedUser = this.userRepo.save(user);
        return this.modelMapper.map(savedUser, UserDto.class);
    }

    // ✅ UPDATE USER
    @Override
    public UserDto updateUser(UserDto userDto, Integer userId) {
        User user = this.userRepo.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setPassword(this.passwordEncoder.encode(userDto.getPassword())); // ✅ Hash password!
        user.setAbout(userDto.getAbout());

        User updatedUser = this.userRepo.save(user);
        return this.modelMapper.map(updatedUser, UserDto.class);
    }

    // ✅ GET USER BY ID
    @Override
    public UserDto getUserById(Integer userId) {
        User user = this.userRepo.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        return this.modelMapper.map(user, UserDto.class);
    }

    // ✅ GET ALL USERS
    @Override
    public List<UserDto> getAllUsers() {
        List<User> users = this.userRepo.findAll();
        return users.stream()
            .map(user -> this.modelMapper.map(user, UserDto.class))
            .collect(Collectors.toList());
    }

    // ✅ DELETE USER
    @Override
    public void deleteUser(Integer userId) {
        User user = this.userRepo.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        this.userRepo.delete(user);
    }

    // ✅ REGISTER NEW USER WITH ROLE
    @Override
    public UserDto registerNewUser(UserDto userDto) {
        User user = this.modelMapper.map(userDto, User.class);
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));

        // ✅ Assign default role
        Role role = this.roleRepo.findById(AppConstants.NORMAL_USER).get();
            
        user.getRoles().add(role);

        User newUser = this.userRepo.save(user);
        return this.modelMapper.map(newUser, UserDto.class);
    }

    // ✅ CHECK IF EMAIL EXISTS
    @Override
    public boolean existsByEmail(String email) {
        return userRepo.findByEmail(email).isPresent(); // ✅ Fix: return actual result
    }

    // ✅ GET USER BY EMAIL
    @Override
    public UserDto getUserByEmail(String email) {
        User user = userRepo.findByEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
        return this.modelMapper.map(user, UserDto.class);
    }

    // ✅ CHANGE PASSWORDs
    @Override
    public void changePassword(Integer userId, String oldPassword, String newPassword) {
        User user = this.userRepo.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new IllegalArgumentException("Old password doesn't match");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        this.userRepo.save(user);
    }

    // ✅ PLACEHOLDER METHODS (Unimplemented)
    @Override
    public void forgotPassword(String email) {
        // Not implemented
    }

    @Override
    public void assignRole(Integer userId, String roleName) {
        // Not implemented
    }

    @Override
    public void removeRole(Integer userId, String roleName) {
        // Not implemented
    }

    @Override
    public void deactivateUser(Integer userId) {
        // Not implemented
    }

    @Override
    public void activateUser(Integer userId) {
        // Not implemented
    }

    @Override
    public List<UserDto> searchUsers(String keyword) {
        // Not implemented
        return null;
    }

    @Override
    public List<UserDto> getUsersByRole(String role) {
        // Not implemented
        return null;
    }
  
}
