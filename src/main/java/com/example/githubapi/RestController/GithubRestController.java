package com.example.githubapi.RestController;

import com.example.githubapi.Response.RepositoryResponse;
import com.example.githubapi.Service.GithubService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/github")
public class GithubRestController {

    private final GithubService githubService;


    @GetMapping("/users/{username}/repos")
    public List<RepositoryResponse> listRepositories(@PathVariable String username) {
        return githubService.getRepositories(username);
    }
}


