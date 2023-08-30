package com.restaurant.app.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseHandler {

    public static ResponseEntity<String> generateError(String message, HttpStatus status) {
        return new ResponseEntity<>(message, status);
    }
}
