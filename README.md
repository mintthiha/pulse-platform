# Pulse — A Release Intelligence Platform

Pulse is a full-stack release intelligence platform that helps engineering teams and developers assess regression risk before deploying. Given a Jira ticket, Pulse returns qualified test scenarios filtered by tags, surfaces linked pull requests, and enables one-click build execution — all from a single dashboard.

---

## The Problem

In large codebases, running the full regression suite on every release is too slow and costly. The most important question becomes: **What are the tests that are affected by this change?** Without dedicated tooling, engineers manually cross-reference Jira tickets, test suites, and PR branches; hoping to cover enough regression without missing something critical. This process is slow, error-prone, and a persistent source of release delays.

## The Solution

Pulse automates regression scoping. Enter a Jira ticket ID and Pulse:

1. Fetches the ticket details from Jira
2. Retrieves all test results tagged to that ticket from its database
3. Filters scenarios by report tags and action tags
4. Surfaces linked pull requests from GitHub
5. Optionally uses AI to expand the tag set based on the ticket description and PR diff
6. Lets you trigger a GitHub Actions regression run on the PR branch with one click

---

## Tech Stack

**Backend**
- Java 21, Spring Boot 3.5, Maven
- PostgreSQL 16 with Flyway migrations
- Spring Security with stateless JWT authentication
- Spring Web `RestClient` for Jira, GitHub, and OpenAI API calls

**Frontend**
- Next.js 15 (App Router), TypeScript
- Tailwind CSS

**Integrations**
- Jira Cloud REST API v3
- GitHub REST API (PR fetch + `workflow_dispatch` trigger)
- OpenAI API (`gpt-4o-mini` for tag suggestion)
- GitHub Actions (nightly test runner + regression runner)

**Infrastructure**
- Docker + Docker Compose (PostgreSQL)
- Flyway (schema versioning)

---

## Architecture

```
pulse-frontend (Next.js)
        │
        │  JWT-authenticated REST calls
        ▼
pulse-platform (Spring Boot)
        │
        ├── /api/auth        → register, login (returns JWT)
        ├── /api/tickets/:id → fetches ticket from Jira Cloud
        ├── /api/runs/:id    → returns test results by ticket ID
        ├── /api/prs/:id     → fetches linked PRs from GitHub
        ├── /api/builds      → triggers workflow_dispatch on GitHub Actions
        └── /api/ai          → calls OpenAI for tag expansion
        │
        ▼
PostgreSQL
  └── test_results (scenario_name, ticket_id, status, report_tags, action_tags, ...)
  └── users (username, bcrypt_password, jira_token, github_token)
```

---

## Tag-Based Filtering

Tests are tagged at two levels:

- **Report tags** — broad application areas (e.g. `PWM`, `Technology`, `RiskManagement`)
- **Action tags** — specific features (e.g. `BondOrder`, `EquityOrder`, `OrderCancellation`)

When a ticket is analyzed, Pulse filters the test results database by these tags to return only the scenarios relevant to the change.

**AI Mode** — clicking "Enhance with AI" sends the ticket description and PR branch info to OpenAI, which returns additional suggested tags. These are merged with the existing tags and the scenario list is re-filtered, compensating for inconsistent tagging in the test suite.

---

## Project Structure

```
pulse-platform/
├── src/main/java/com/pulse/
│   ├── auth/               # JWT auth (register, login)
│   ├── ticket/             # Jira integration
│   ├── test/               # Test result storage and retrieval
│   ├── pr/                 # GitHub PR integration
│   ├── build/              # GitHub Actions trigger
│   ├── ai/                 # OpenAI tag suggestion
│   └── common/
│       └── config/         # Security, CORS, properties
├── src/main/resources/
│   ├── application.yml
│   └── db/migration/       # Flyway migrations (V1–V4)
└── docker-compose.yml
```

---

## Getting Started

### Prerequisites

- Java 21
- Maven
- Docker

### Setup

**1. Clone the repository**

```bash
git clone https://github.com/mintthiha/pulse-platform.git
cd pulse-platform
```

**2. Start PostgreSQL**

```bash
docker compose up -d
```

**3. Configure environment**

Copy `application.yml.example` to `application.yml` and fill in the values:

```yaml
jira:
  base-url: https://your-site.atlassian.net
  email: your-email@example.com
  api-token: your-jira-api-token

github:
  token: your-github-pat
  owner: your-github-username
  repo: your-test-repo

openai:
  api-key: your-openai-api-key
```

**4. Run the backend**

```bash
./mvnw spring-boot:run
```

The API will be available at `http://localhost:8080`.

**5. Register a user**

```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"youruser","password":"yourpassword"}'
```

---

## API Reference

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| POST | `/api/auth/register` | Public | Register a new user |
| POST | `/api/auth/login` | Public | Login, returns JWT |
| GET | `/api/tickets/:id` | JWT | Fetch Jira ticket |
| GET | `/api/runs/:id` | JWT | Get test results by ticket |
| POST | `/api/runs` | Public | Post test results (CI/CD) |
| GET | `/api/prs/:id` | JWT | Get linked PRs from GitHub |
| POST | `/api/builds/trigger` | JWT | Trigger GitHub Actions workflow |
| POST | `/api/ai/suggest-tags` | JWT | Get AI-suggested tags |

---

## Demo Repo

Test scenarios and GitHub Actions workflows live in [pulse-demo-tests](https://github.com/mintthiha/pulse-demo-tests), which simulates a real test repository. The nightly workflow posts test results to the Pulse API, keeping the database populated with fresh run data.

---

## Design Principles

- **No overengineering** — tag-based filtering first, AI as an optional enhancement
- **Fail fast** — illegal argument exceptions on missing preconditions, Flyway validates schema on startup
- **One correct path** — constructor injection only, Flyway for all schema changes, BCrypt for all passwords
- **Separation of concerns** — Controller → Service → Repository, each with a single responsibility

---

## License

MIT
