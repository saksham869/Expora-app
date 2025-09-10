package com.example.demo.controllers;

import com.example.demo.payloads.JwtAuthRequest;
import com.example.demo.payloads.JwtAuthResponse;
import com.example.demo.payloads.UserDto;
import com.example.demo.services.UserService;
import com.example.demo.Security.JwtTokenHelper;
import com.example.demo.exceptions.ApiException;
import com.example.demo.Security.CustomUserDetailService;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private JwtTokenHelper jwtTokenHelper;

    @Autowired
    private CustomUserDetailService customUserDetailService;

    @Autowired
    private AuthenticationManager authenticationManager;

    // LOGIN ENDPOINT
    @Autowired
    private UserService userService;
    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponse> createToken(@RequestBody JwtAuthRequest request) throws Exception {

        // 1. Authenticate the username and password
        authenticate(request.getUsername(), request.getPassword());

        // 2. Load user details (like roles/authorities)
        UserDetails userDetails = customUserDetailService.loadUserByUsername(request.getUsername());

        // 3. Generate JWT token using user details
        String token = jwtTokenHelper.generateToken(userDetails);

        // 4. Prepare the response object with token, username, and roles
        JwtAuthResponse response = new JwtAuthResponse();
        response.setToken(token); // Set the JWT token
        response.setUsername(userDetails.getUsername()); // Set the username
        response.setRoles(userDetails.getAuthorities() // Convert roles to List<String>
            .stream()
            .map(auth -> auth.getAuthority())
            .collect(Collectors.toList()));

        // 5. Return the response with HTTP 200 OK
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    

    // METHOD TO AUTHENTICATE USER
    private void authenticate(String username, String password) {
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(username, password);
        try {
            this.authenticationManager.authenticate(authToken);
        } catch (BadCredentialsException e) {
            throw new ApiException("Invalid username or password!");
        }
    }
    // register new api 
    @PostMapping("/register")
    public ResponseEntity<UserDto> registerUser(@RequestBody UserDto userDto){
    	UserDto registeredUser = this.userService.registerNewUser(userDto);
    	return new ResponseEntity<UserDto>(registeredUser, HttpStatus.CREATED);
    	
    	
    }
}
