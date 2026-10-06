# Shipment Disruption Risk

A full-stack application for detecting and scoring disruption risk in supply chain shipments.

This project is being developed as a final-year research project. It combines a Spring Boot backend, a React frontend, and a rule-based risk engine validated against a public shipment dataset.

## Repository Structure

```
shipment-disruption-risk/
├── apps/
│   └── backend/              Spring Boot REST API
├── docs/                     BRD, SRS, ERD, architecture notes
├── infra/                    Docker Compose, deployment configs
├── .github/workflows/        CI pipelines
├── .editorconfig
├── .gitignore
└── README.md
```

The `frontend/` application will be added under `apps/` in a later phase.

## Tech Stack

**Backend**
- Java 17
- Spring Boot 4.1.1
- Spring Data JPA
- Spring Security
- Spring Data Redis
- PostgreSQL 15
- Lombok
- Gradle

**Frontend** (planned)
- React
- Vite
- Axios

**DevOps**
- Docker and Docker Compose
- GitHub Actions
- H2 (for integration tests)

## Getting Started

### Prerequisites

- JDK 17
- Docker Desktop
- Node.js 18+ (for the frontend phase)

### 1. Start the Database

From the `infra/` folder:

```
cd infra
docker compose up -d
```

This starts PostgreSQL 15 on `localhost:5433` with the database `shipment_db`.

### 2. Run the Backend

From the `apps/backend/` folder:

```
cd apps/backend
./gradlew bootRun
```

The API runs at `http://localhost:8080`.

### 3. Run the Frontend

Not yet implemented. Instructions will be added when the frontend is built.

## Testing

From the `apps/backend/` folder:

```
./gradlew test
```

Current test coverage:

- Unit tests for `UserServiceImpl` (Mockito)
- Integration tests for `UserRepository` (`@DataJpaTest` with H2)

All tests run without requiring Docker or PostgreSQL.

## Project Status

- [x] Backend project structure
- [x] User entity, repository, service
- [x] User unit tests (Mockito)
- [x] User integration tests (H2)
- [ ] Shipment entity and CSV ingestion
- [ ] Risk rule engine
- [ ] JWT authentication
- [ ] Shipment read API
- [ ] Analytics endpoints
- [ ] React frontend
- [ ] Deployment

## Documentation

Design documents live in the `docs/` folder:

- `BRD.md` — Business Requirements Document
- `SRS.md` — System Requirements Specification
- `ERD.md` — Entity-Relationship Diagram and data model
- `architecture.md` — System architecture and design decisions

These will be added as the project progresses.

## License

This project is part of a final-year research project and is not currently licensed for external use.
