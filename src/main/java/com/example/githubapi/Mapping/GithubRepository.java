package com.example.githubapi.Mapping;

public record GithubRepository(String name, GithubOwner owner, boolean fork) {}

