package com.example.task;

import com.example.task.models.Branch;
import com.example.task.models.Repository;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;


public class RepositoryServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private RepositoryService repositoryService;

    public RepositoryServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetRepositoriesSuccess() throws IOException {
        String username = "testUser";
        ResponseEntity responseEntity = new ResponseEntity<>("Success", HttpStatus.OK);
        when(restTemplate.getForEntity(anyString(),  eq(Object[].class))).thenReturn(responseEntity);
        List<Map<String, Object>> list = new ArrayList<>();
        list.add(Map.of("name", "name1", "owner", Map.of("login", "login1"), "commit", Map.of("sha", "sha1")));
        when(objectMapper.readValue((String) null, List.class)).thenReturn(list);
        ResponseEntity<?> result = repositoryService.getUserRepositories(username);
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }
    @Test
    public void testGetUserRepositories_UserNotFound() throws JsonProcessingException {
        String username = "testUser";
        ResponseEntity responseEntity = new ResponseEntity<>("No Success", HttpStatus.NOT_FOUND);
        when(restTemplate.getForEntity(anyString(),  eq(Object[].class))).thenThrow(HttpClientErrorException.NotFound.class);
        List<Map<String, Object>> list = new ArrayList<>();
        when(objectMapper.readValue((String) null, List.class)).thenReturn(list);
        ResponseEntity<?> result = repositoryService.getUserRepositories(username);
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    @Test
    public void testGetUserRepositories_InternalServerError() throws JsonProcessingException {
        String username = "testUser";
        ResponseEntity responseEntity = new ResponseEntity<>("No Success", HttpStatus.NOT_FOUND);
        when(restTemplate.getForEntity(anyString(),  eq(Object[].class))).thenReturn(responseEntity);
        List<Map<String, Object>> list = new ArrayList<>();
        when(objectMapper.readValue((String) null, List.class)).thenThrow(JsonProcessingException.class);
        ResponseEntity<?> result = repositoryService.getUserRepositories(username);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
    }
}