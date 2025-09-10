package com.example.demo.exceptions;

/**
 * Custom exception for API-related errors.
 */
public class ApiException extends RuntimeException {

    private static final long serialVersionUID = 1L; // Optional but recommended for Serializable classes

    public ApiException(String message) {
        super(message);
    }

    public ApiException() {
        super();
    }
}
