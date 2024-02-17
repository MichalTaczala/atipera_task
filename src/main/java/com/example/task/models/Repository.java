package com.example.task.models;

import java.util.List;

public record Repository(String repositoryName, String ownerLogin, List<Branch> branches) {
}
