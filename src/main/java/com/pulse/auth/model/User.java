package com.pulse.auth.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", nullable = false, unique = true, length = 100)
    private String username;

    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Column(name = "jira_token", length = 500)
    private String jiraToken;

    @Column(name = "github_token", length = 500)
    private String githubToken;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}