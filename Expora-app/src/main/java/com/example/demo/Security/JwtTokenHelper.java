package com.example.demo.Security;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenHelper {

    // Token validity set to 5 hours (in seconds)
    public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;

    // Secret key string (must be at least 32 characters for HS256)
    private final String SECRET_STRING = "jwtTokenKeyjwtTokenKeyjwtTokenKey1234";

    // Convert the string into a cryptographic key
    private final SecretKey secretKey = Keys.hmacShaKeyFor(SECRET_STRING.getBytes());

    // Extract username (subject) from token
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    // Extract expiration date from token
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    // Extract a single claim using a resolver function
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    // Retrieve all claims from the token using JJWT v0.12.6+
    private Claims getAllClaimsFromToken(String token) {
        JwtParser parser = Jwts.parser()
                                .verifyWith(secretKey)
                                .build();

        return parser.parseSignedClaims(token).getPayload(); // get the claims body
    }

    // Check if the token has expired
    private boolean isTokenExpired(String token) {
        return getExpirationDateFromToken(token).before(new Date());
    }

    // Generate token for a user
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, userDetails.getUsername());
    }

    // Create the JWT token
    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                   .claims(claims)
                   .subject(subject)
                   .issuedAt(new Date(System.currentTimeMillis()))
                   .expiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
                   .signWith(secretKey)
                   .compact();
    }

    // Validate token against username and expiration
    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
