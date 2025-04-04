package com.example.githubapi;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@WireMockTest(httpPort = 9090)
public class GithubControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturn404IfUserNotFound() throws Exception {
        stubFor(WireMock.get(urlEqualTo("/users/nonexistentUser/repos"))
                .willReturn(aResponse().withStatus(404).withHeader("Content-Type", "application/json")));

        mockMvc.perform(get("/api/github/users/nonexistentUser/repos")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("User not found"));
    }


    @Test
    void shouldReturn500IfGitHubApiReturnsError() throws Exception {
        stubFor(WireMock.get(urlEqualTo("/users/existingUser/repos"))
                .willReturn(aResponse().withStatus(500)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"message\": \"Internal server error\"}")));

        mockMvc.perform(get("/api/github/users/existingUser/repos")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.message").value("GitHub API error"));
    }

}

