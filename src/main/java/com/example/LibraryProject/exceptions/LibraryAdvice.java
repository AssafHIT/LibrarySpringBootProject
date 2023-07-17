package com.example.LibraryProject.exceptions;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.tomcat.jni.LibraryNotFoundError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
@RestControllerAdvice
public class LibraryAdvice {


    @ExceptionHandler(LibraryNotFoundException.class)
    public ResponseEntity<String> handleLibraryNotFoundException(LibraryNotFoundException ex) {
        String errorMessage = ex.getMessage();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
    }

    private Map<String, Object> getStringMessage(String message) {
        Map<String, Object> body = new LinkedHashMap<>();

        body.put("time", LocalDateTime.now());
        body.put("message", message);
        return body;
    }

    // Handle undefined URL
    @ExceptionHandler(value = { NoHandlerFoundException.class })
    public ResponseEntity<Object> handleUndefinedUrlException(NoHandlerFoundException ex) {
        String errorMessage = "The requested URL is not found.";
        HttpStatus status = HttpStatus.NOT_FOUND;

        URLError apiError = new URLError(status, errorMessage);
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }
    @Data
    @AllArgsConstructor
    public class URLError {
        private HttpStatus status;
        private String message;
    }
}
