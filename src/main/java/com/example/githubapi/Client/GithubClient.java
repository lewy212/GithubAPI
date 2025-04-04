package com.example.githubapi.Client;

import com.example.githubapi.Mapping.GithubBranch;
import com.example.githubapi.Mapping.GithubRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class GithubClient {

    private final RestTemplate restTemplate;

    @Value("${github.url}")
    private String githubUrl;

    public List<GithubRepository> getRepositories(String username) {
        String url = githubUrl + "/users/" + username + "/repos";
        try {
            GithubRepository[] repositories = restTemplate.getForObject(url, GithubRepository[].class);
            return repositories != null ? Arrays.asList(repositories) : List.of();
        } catch (HttpClientErrorException.NotFound e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "GitHub API error");
        }
    }

    public List<GithubBranch> getBranches(String username, String repoName) {
        String url = githubUrl + "/repos/" + username + "/" + repoName + "/branches";
        try {
            GithubBranch[] branches = restTemplate.getForObject(url, GithubBranch[].class);
            return branches != null ? Arrays.asList(branches) : List.of();
        } catch (Exception e) {
            return List.of();
        }
    }

}
