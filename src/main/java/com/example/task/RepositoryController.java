package com.example.task;

import com.example.task.models.Branch;
import com.example.task.models.Repository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/repositories")
public class RepositoryController {


    private final RepositoryService repositoryService;

    public RepositoryController(RepositoryService repositoryService) {
        this.repositoryService = repositoryService;
    }

    @GetMapping("/{username}")
    public ResponseEntity<Object> getUserRepositories(@PathVariable String username,@RequestHeader(value = "Accept", required = true) String header) {
        if (header != null && header.equals("application/json")) {
            return repositoryService.getUserRepositories(username);
        }
        return new ResponseEntity<>("Invalid Accept header", HttpStatus.BAD_REQUEST);
    }
}
