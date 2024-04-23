package org.ldms.app.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.logging.Logger;

@RestControllerAdvice
public class ControllerAdvice {
    private static final Logger LOGGER = Logger.getLogger(ControllerAdvice.class.getName());
    @ExceptionHandler({WebExchangeBindException.class, HttpMessageNotReadableException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleBindingErrors(WebExchangeBindException e) {
        return ResponseEntity.badRequest().body("Invalid request: " + e.getAllErrors().get(0).getDefaultMessage());
    }

    @ExceptionHandler({MissingServletRequestParameterException.class, MethodArgumentTypeMismatchException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleParameterErrors(WebExchangeBindException e) {
        return ResponseEntity.badRequest().body("Invalid request: " + e.getAllErrors().get(0).getDefaultMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleIllegalArgumentErrors(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body("Invalid request: " + e.getMessage());
    }
}