package com.example.demo.payloads;

import lombok.Data;
import java.util.List;

@Data
public class JwtAuthResponse {
    private String token;
    private String tokenType = "Bearer";
    private String username;
    private List<String> roles;
}
