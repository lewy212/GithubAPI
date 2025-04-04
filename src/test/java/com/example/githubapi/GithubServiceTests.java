package com.example.githubapi;

import com.example.githubapi.Response.RepositoryResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.example.githubapi.Service.GithubService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
@WireMockTest(httpPort = 9090)
public class GithubServiceTests {

    @Autowired
    private GithubService githubService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void shouldReturnRepositories_whenUserExists()  {

        String jsonResponse = """
                [
                    {
                        "name": "repo1",
                        "owner": { "login": "testUser" },
                        "fork": false,
                         "branches": [
                                    {
                                        "name": "master",
                                        "lastCommitSha": "da67856619c71910acf79b5bfe12078ffefaec8a"
                                    },
                                    {
                                        "name": "zSerwerem",
                                        "lastCommitSha": "202123b1fee0949dc94ae9b58f152a47d0f02465"
                                    }
                                ]
                    },
                    {
                        "name": "repo2",
                        "owner": { "login": "testUser" },
                        "fork": true
                    }
                ]
                """;

        stubFor(get(urlEqualTo("/users/testUser/repos"))
                .willReturn(okJson(jsonResponse)
                        .withHeader("Content-Type", "application/json")));

        List<RepositoryResponse> response = githubService.getRepositories("testUser");

        for (RepositoryResponse repositoryResponse : response) {
            System.out.println(repositoryResponse.toString());
        }

        assertThat(response).isNotNull();
        assertThat(response).hasSize(1);

        assertThat(response.get(0).repositoryName()).isEqualTo("repo1");
        assertThat(response.get(0).ownerLogin()).isEqualTo("testUser");
        verify(getRequestedFor(urlEqualTo("/users/testUser/repos")));
    }
    @Test
    void shouldThrowNotFound_whenUserDoesNotExist() {


        stubFor(get(urlEqualTo("/users/nonexistentUser/repos"))
                .willReturn(aResponse().withStatus(404)));

        assertThatThrownBy(() -> githubService.getRepositories("nonexistentUser"))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("404 NOT_FOUND \"User not found\"");

        verify(getRequestedFor(urlEqualTo("/users/nonexistentUser/repos")));
    }
    @Test
    void shouldThrowInternalServerError_whenGitHubApiFails() {

        stubFor(get(urlEqualTo("/users/testUser/repos"))
                .willReturn(aResponse().withStatus(500)));

        assertThatThrownBy(() -> githubService.getRepositories("testUser"))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("500 INTERNAL_SERVER_ERROR \"GitHub API error\"");

        verify(getRequestedFor(urlEqualTo("/users/testUser/repos")));
    }


}
