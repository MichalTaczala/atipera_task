package com.example.task;

import com.example.task.models.Branch;
import com.example.task.models.Repository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RepositoryService {

    @Value("${github.api.url}")
    private String githubApiUrl;
    private ObjectMapper objectMapper = new ObjectMapper();


    public ResponseEntity getUserRepositories(String username) {
        RestTemplate restTemplate = new RestTemplate();
        String url = githubApiUrl + "/users/" + username + "/repos";

        ResponseEntity<Object[]> responseFromGithub;
        List<Repository> repositoriesResponse = new ArrayList<>();

        try {
            responseFromGithub = restTemplate.getForEntity(url, Object[].class);
            if (responseFromGithub.getBody() != null) {
                try {
                    List<Map<String, Object>> detectedRepositories = objectMapper.readValue(objectMapper.writeValueAsString(responseFromGithub.getBody()), List.class);

                    for (Map<String, Object> repositoryData: detectedRepositories) {
                        Repository repository;

                        String repositoryName = (String) repositoryData.get("name");

                        Map<String, Object> owner = (Map<String, Object>) repositoryData.get("owner");
                        String ownerLogin = (String) owner.get("login");

                        //branches
                        ResponseEntity<Object[]> responseFromGithubBranches;
                        responseFromGithubBranches = restTemplate.getForEntity(githubApiUrl + "/repos/" + ownerLogin + "/" + repositoryName + "/branches", Object[].class);
                        List<Map<String, Object>> detectedBranches = objectMapper.readValue(objectMapper.writeValueAsString(responseFromGithubBranches.getBody()), List.class);
                        List<Branch> branches = new ArrayList<>();
                        for (Map<String, Object> branchData: detectedBranches) {
                            String branchName = (String) branchData.get("name");

                            Map<String, Object> commit = (Map<String, Object>) branchData.get("commit");
                            String commitSha = (String) commit.get("sha");
                            Branch branch = new Branch(branchName, commitSha);
                            branches.add(branch);
                        }
                        repository = new Repository(repositoryName, ownerLogin, branches);
                        repositoriesResponse.add(repository);


                    }
                } catch (IOException e) {
                    Map<String, String> error = new HashMap<>();
                    error.put("status", String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()));
                    error.put("message", "Internal server error");
                    return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }



        } catch (HttpClientErrorException.NotFound e) {
            Map<String, String> error = new HashMap<>();
            error.put("status", String.valueOf(HttpStatus.NOT_FOUND.value()));
            error.put("message", username + " user doesn't exist");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(repositoriesResponse, HttpStatus.OK);
    }

    void getBranchesFromRepository(String ownerLogin, String repositoryName) {

    }

}
