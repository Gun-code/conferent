package com.conferent.exceptions;

public class DuplicateReservationException extends RuntimeException {
    
    public DuplicateReservationException(String message) {
        super(message);
    }
    
    public DuplicateReservationException(String message, Throwable cause) {
        super(message, cause);
    }
} 