# Newsletter Dashboard

A dashboard application for tracking and displaying newsletter metrics.

## Project Structure

- `frontend/` - React frontend application
- `backend/` - Spring Boot backend application
- `docker/` - Docker configurations for development

## Prerequisites

- Node.js (v18 or later)
- Java 17
- Docker and Docker Compose
- Gradle (install via `brew install gradle` on macOS)

## Setup

1. Install Gradle (macOS):
```bash
brew install gradle
```

2. Install root project dependencies:
```bash
npm install
```

3. Install frontend dependencies:
```bash
cd frontend && npm install
```

4. Initialize Gradle wrapper:
```bash
cd backend && gradle wrapper
```

5. Start the PostgreSQL database:
```bash
npm run db:up
```

## Development

To run both frontend and backend concurrently:
```bash
npm start
```

Or run them separately:

- Frontend only:
```bash
npm run frontend
# Access at http://localhost:5173
```

- Backend only:
```bash
npm run backend
# Access at http://localhost:8080
```

- Stop database:
```bash
npm run db:down
```

## Testing

Run all tests:
```bash
npm test
```

Or run tests separately:

- Frontend tests:
```bash
npm run test:frontend
```

- Backend tests:
```bash
npm run test:backend
```

## Frontend Details

- Built with React + Vite
- TypeScript for type safety
- React Testing Library + Vitest for testing
- MSW for API mocking in tests

## Backend Details

- Spring Boot application
- PostgreSQL database
- Liquibase for database migrations
- JUnit 5 + TestContainers for testing

## API Documentation

Backend API endpoints will be available at:
- Swagger UI: http://localhost:8080/swagger-ui.html
- API Docs: http://localhost:8080/v3/api-docs

## Database Migrations

Database migrations are managed by Liquibase and run automatically on application startup. Migration files are located in:
```
backend/src/main/resources/db/changelog/
```

## Project Files

- `.gitignore` - Configured to exclude build artifacts, IDE files, and environment-specific files
- `package.json` - Root project configuration with scripts to manage both frontend and backend
- `docker/docker-compose.yml` - PostgreSQL container configuration
- `backend/build.gradle` - Backend build configuration
- `backend/settings.gradle` - Backend project settings
