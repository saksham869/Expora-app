//package com.example.demo.Security;
//
//import java.io.IOException;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import com.example.demo.exceptions.GlobalExceptionHandler;
//
//import io.jsonwebtoken.ExpiredJwtException;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//
//@Component
//public class JwtAuthenticationFilter extends OncePerRequestFilter {
//
//    // Optional: To handle exceptions globally
//    private final GlobalExceptionHandler globalExceptionHandler;
//
//    @Autowired
//    private UserDetailsService userDetailsService;
//
//    @Autowired
//    private JwtTokenHelper jwtTokenHelper;
//
//    // Constructor injection
//    JwtAuthenticationFilter(GlobalExceptionHandler globalExceptionHandler) {
//        this.globalExceptionHandler = globalExceptionHandler;
//    }
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//            throws ServletException, IOException {
//
//        // 1️⃣ Get JWT Token from Authorization Header
//        String requestToken = request.getHeader("Authorization");
//        //Bearer 5643645xvnf
//        System.out.println(requestToken);
//        String username = null;
//        String token = null;
//
//        // 2️⃣ Check if token is present and starts with "Bearer "
//        if (requestToken != null && requestToken.startsWith("Bearer ")) {
//            token = requestToken.substring(7); // Remove "Bearer " prefix
//            try {
//                username = this.jwtTokenHelper.getUsernameFromToken(token);
//            } catch (IllegalArgumentException e) {
//                System.out.println("Unable to get JWT token");
//            } catch (ExpiredJwtException e) {
//                System.out.println("JWT token has expired");
//            }
//        } else {
//            System.out.println("JWT token does not begin with 'Bearer '");
//        }
//
//        // 3️⃣ Token is valid and we got username, but security context is empty
//        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//
//            // 4️⃣ Load user details from database (via UserDetailsService)
//            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
//
//            // 5️⃣ Validate the token
//            if (this.jwtTokenHelper.validateToken(token, userDetails)) {
//
//                // 6️⃣ Create Authentication token
//                UsernamePasswordAuthenticationToken authentication =
//                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//
//                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//
//                // 7️⃣ Set the authentication in the Spring Security context
//                SecurityContextHolder.getContext().setAuthentication(authentication);
//            } else {
//                System.out.println("Invalid JWT token");
//            }
//        } else {
//            System.out.println("Username is null OR security context already has authentication");
//        }
//
//        // ✅ Let the request continue down the filter chain
//        filterChain.doFilter(request, response);
//    }
//
//}

//package com.example.demo.Security;
//
//import java.io.IOException;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import com.example.demo.exceptions.GlobalExceptionHandler;
//import io.jsonwebtoken.ExpiredJwtException;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//@Component
//public class JwtAuthenticationFilter extends OncePerRequestFilter {
//
//    @Autowired
//    private JwtTokenHelper jwtTokenHelper;
//
//    @Autowired
//    private CustomUserDetailService customUserDetailService;
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request,
//                                    HttpServletResponse response,
//                                    FilterChain filterChain) throws ServletException, IOException {
//
//        String path = request.getRequestURI();
//
//        // Skip Swagger + auth endpoints
//        if (path.startsWith("/v3/api-docs") ||
//            path.startsWith("/swagger-ui") ||
//            path.startsWith("/swagger-ui.html") ||
//            path.startsWith("/api/v1/auth")) {
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//        String authHeader = request.getHeader("Authorization");
//        String username = null;
//        String token = null;
//
//        if (authHeader != null && authHeader.startsWith("Bearer ")) {
//            token = authHeader.substring(7);
//            username = jwtTokenHelper.getUsernameFromToken(token);
//        } else {
//            logger.debug("JWT token does not begin with 'Bearer '");
//        }
//
//        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//            UserDetails userDetails = this.customUserDetailService.loadUserByUsername(username);
//            if (jwtTokenHelper.validateToken(token, userDetails)) {
//                UsernamePasswordAuthenticationToken authToken =
//                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                SecurityContextHolder.getContext().setAuthentication(authToken);
//            }
//        }
//
//        filterChain.doFilter(request, response);
//    }
//}
package com.example.demo.Security;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Autowired
    private JwtTokenHelper jwtTokenHelper;

    @Autowired
    private CustomUserDetailService customUserDetailService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();

        // Skip Swagger + Auth endpoints
        if (path.startsWith("/v3/api-docs") ||
            path.startsWith("/swagger-ui") ||
            path.startsWith("/swagger-ui.html") ||
            path.startsWith("/api/v1/auth/")) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");
        String username = null;
        String token = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            try {
                username = jwtTokenHelper.getUsernameFromToken(token);
            } catch (IllegalArgumentException e) {
                logger.error("Unable to parse JWT token: {}", e.getMessage());
            } catch (ExpiredJwtException e) {
                logger.warn("JWT token expired: {}", e.getMessage());
            } catch (MalformedJwtException | SignatureException e) {
                logger.error("Invalid JWT token: {}", e.getMessage());
            }
        } else {
            logger.debug("JWT token is missing or does not begin with 'Bearer '");
        }

        // Authenticate user if token is valid
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = customUserDetailService.loadUserByUsername(username);

            if (jwtTokenHelper.validateToken(token, userDetails)) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            } else {
                logger.warn("JWT token validation failed for user: {}", username);
            }
        }

        filterChain.doFilter(request, response);
    }
}

