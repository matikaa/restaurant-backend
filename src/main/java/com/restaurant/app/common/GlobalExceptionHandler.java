package com.restaurant.app.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static com.restaurant.app.common.ConstantValues.INCORRECT_LOG_DATA;
import static com.restaurant.app.common.ConstantValues.NO_ACCESS;
import static org.springframework.http.HttpStatus.FORBIDDEN;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Void> handleAccessDeniedException(AccessDeniedException ex) {
        LOGGER.warn(NO_ACCESS);
        return ResponseEntity.status(FORBIDDEN).build();
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Void> handleAuthenticationException(AuthenticationException ex) {
        LOGGER.warn(INCORRECT_LOG_DATA);
        return ResponseEntity.notFound().build();
    }
}
