package com.api.appointmentservice.exception;

public class RequestSpamException extends RuntimeException {
    public RequestSpamException(String message) {
        super(message);
    }
}
