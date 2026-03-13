package com.pulse.common.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    // Should return 400 with message when IllegalArgumentException is thrown
    @Test
    void handleIllegalArgument_returns400() {
        ResponseEntity<Map<String, Object>> response =
                handler.handleIllegalArgument(new IllegalArgumentException("Branch must not be null or empty"));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).containsEntry("status", 400);
        assertThat(response.getBody()).containsEntry("message", "Branch must not be null or empty");
    }

    // Should return 404 with message when NoSuchElementException is thrown
    @Test
    void handleNotFound_returns404() {
        ResponseEntity<Map<String, Object>> response =
                handler.handleNotFound(new NoSuchElementException("Ticket not found"));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).containsEntry("status", 404);
        assertThat(response.getBody()).containsEntry("message", "Ticket not found");
    }

    // Should return 500 with generic message for unhandled exceptions
    @Test
    void handleGeneric_returns500() {
        ResponseEntity<Map<String, Object>> response =
                handler.handleGeneric(new RuntimeException("Something broke"));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).containsEntry("status", 500);
        assertThat(response.getBody()).containsEntry("message", "An unexpected error occurred");
    }
}