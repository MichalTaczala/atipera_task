package com.example.task;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RepositoryControllerTest {

    @Mock
    private RepositoryService repositoryService;

    @InjectMocks
    private RepositoryController repositoryController;

    @Test
    public void testGetUserRepositories_ValidAcceptHeader() {
        // Arrange
        String username = "testUser";
        String acceptHeader = "application/json";
        ResponseEntity<Object> expectedResponse = ResponseEntity.ok().build();
        when(repositoryService.getUserRepositories(username)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<Object> response = repositoryController.getUserRepositories(username, acceptHeader);

        // Assert
        assertEquals(expectedResponse, response);
    }

    @Test
    public void testGetUserRepositories_InvalidAcceptHeader() {
        // Arrange
        String username = "testUser";
        String acceptHeader = "invalid-header";

        // Act
        ResponseEntity<Object> response = repositoryController.getUserRepositories(username, acceptHeader);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid Accept header", response.getBody());
    }
}