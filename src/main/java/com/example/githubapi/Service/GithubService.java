package com.example.githubapi.Service;

import com.example.githubapi.Mapping.GithubBranch;
import com.example.githubapi.Response.BranchResponse;
import com.example.githubapi.Response.RepositoryResponse;
import com.example.githubapi.Client.GithubClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GithubService {

    private final GithubClient githubClient;

    public List<RepositoryResponse> getRepositories(String username) {
        return githubClient.getRepositories(username).stream()
                .filter(repo -> !repo.fork())
                .map(repo -> new RepositoryResponse(
                        repo.name(),
                        repo.owner().login(),
                        mapBranches(githubClient.getBranches(username, repo.name()))
                ))
                .collect(Collectors.toList());
    }

    private List<BranchResponse> mapBranches(List<GithubBranch> branches) {
        return branches.stream()
                .map(branch -> new BranchResponse(branch.name(), branch.commit().sha()))
                .collect(Collectors.toList());
    }
}
