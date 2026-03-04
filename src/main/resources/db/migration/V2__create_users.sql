CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    jira_token VARCHAR(500),
    github_token VARCHAR(500),
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);