# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

BugHuntr is an intentionally vulnerable bug bounty management platform for security training/testing. It has a Spring Boot 3.5.10 backend (Java 17) and an Angular 17 frontend served as static resources. The application uses H2 in PostgreSQL-compatibility mode by default, with PostgreSQL as the production database.

## Build & Run Commands

```bash
# Frontend (Angular 17)
cd Angular && npm install && npx ng build

# Backend build (Maven wrapper available in .mvn/wrapper/)
./mvnw clean install

# Run application (port 8080)
./mvnw spring-boot:run

# Run tests
./mvnw test

# OWASP dependency vulnerability scan
./mvnw dependency-check:check
# Report at: target/dependency-check-report.html
```

The Angular build output goes to `src/main/resources/static/` and is served by Spring Boot at `http://localhost:8080`.

H2 Console: `http://localhost:8080/h2-console/` (JDBC URL: `jdbc:h2:file:./bughuntrdb;MODE=PostgreSQL;DB_CLOSE_DELAY=-1;DATABASE_TO_LOWER=TRUE`, user: `sa`, no password).

## Architecture

**Three-tier architecture** with non-standard source layout (`src/main` instead of `src/main/java`):

```
src/main/com/tcs/utx/digiframe/
├── controller/    — 19 REST controllers (@RestController, base path /BugHuntr/api/)
├── service/       — Interface + Impl pattern (e.g., VulnerabilityService/ServiceImpl)
├── dao/           — @Repository classes using JdbcTemplate directly (no JPA repositories)
├── model/         — Entity/domain classes
├── dto/           — Data transfer objects
├── exception/     — Custom exceptions (UserDefinedException, APIError)
├── util/          — Utilities (CSRFValidationFilter, TSSVStringUtil)
└── SecurityConfig.java, BughuntrApplication.java
```

**Key architectural decisions:**
- **No JPA repositories** — all DB access is via `JdbcTemplate` with raw SQL in `dao/BugHuntrQueryConstants.java` (~758 lines of SQL constants)
- **SQL dialect switching** — `applicationSpecific.properties` sets `DatabaseName` (Postgres/Oracle) and DAOs select queries accordingly
- **CSRF** — custom dual-validation filter (session + cookie tokens) in `CSRFValidationFilter.java`, wrapped by `EnableCSRFFilter`
- **Session** — configured for Redis store with 10-minute timeout
- **Static frontend** — Angular build artifacts live in `src/main/resources/static/`

## Security Configuration

In `SecurityConfig.java`:
- Public endpoints: `/Bughuntr/isAuthUser`, `/Bughuntr/login`, `/BugHuntr/api/dologin`, `/BugHuntr/api/menu`, static resources
- All other `/BugHuntr/api/*` endpoints require authentication
- CSRF exempt: `/h2-console/**`, `/BugHuntr/api/dologin`
- Custom 401 handlers: `CustomAuthenticationEntryPoint`, `CustomAccessDeniedHandler`

## User Roles

Admin, Bounty Administrator, Researcher, Visitor, Guest — permissions managed via `PermissionHelperService`.

## Important Notes

- **Do NOT modify database credentials** in `application.properties`
- Database init script: `src/main/resources/h2-backup.sql`
- Logging: Log4j2, Hibernate SQL at DEBUG level

---

## Security Pentesting & Remediation Guide

This project contains **intentional security vulnerabilities** across 12 OWASP categories. The goal is to find and fix all of them.

### Target Vulnerability Categories

| # | Category | Primary Locations to Audit |
|---|----------|---------------------------|
| 1 | SQL Injection | `dao/*DAOImpl.java`, `BugHuntrQueryConstants.java` — look for string concatenation in SQL |
| 2 | Broken Access Control | `SecurityConfig.java`, all controllers — missing role checks on endpoints |
| 3 | IDOR | Controllers taking ID params (`/vulnerability/{id}`, `/researcher/{id}`, etc.) — missing ownership validation |
| 4 | CSRF | `CSRFValidationFilter.java`, `EnableCSRFFilter.java`, `SecurityConfig.java` — bypasses or missing enforcement |
| 5 | OS Command Injection | Any use of `Runtime.exec()`, `ProcessBuilder` — look in controllers and services |
| 6 | Path Traversal | File operations (`File`, `Path`, `InputStream`) — look for unsanitized user-supplied paths |
| 7 | Sensitive Info Disclosure | Error handlers, stack traces in responses, hardcoded secrets, debug endpoints, H2 console exposure |
| 8 | URL Redirect | `sendRedirect()`, `redirect:` returns, `Location` headers — unvalidated redirect targets |
| 9 | Insecure Deserialization | `ObjectInputStream.readObject()`, `XMLDecoder`, Jackson polymorphic typing |
| 10 | Improper Input Validation | `@RequestParam`, `@PathVariable`, `@RequestBody` — missing `@Valid`, `@Pattern`, `@Size` |
| 11 | Vulnerable Dependencies | `pom.xml`, `Angular/package.json` — run OWASP dependency-check and `npm audit` |
| 12 | Header Manipulation | `setHeader()`/`addHeader()` with user input, missing security headers (CSP, X-Frame-Options, etc.) |

### Remediation Standards

When fixing vulnerabilities, apply these patterns:

**SQL Injection:** Always use parameterized queries with `?` placeholders in JdbcTemplate. Never concatenate user input into SQL strings. Example:
```java
// BAD:  "SELECT * FROM users WHERE id = " + userId
// GOOD: "SELECT * FROM users WHERE id = ?", new Object[]{userId}
```

**Broken Access Control / IDOR:** Add `@PreAuthorize` annotations or manual role/ownership checks. Verify the authenticated user has permission to access the requested resource. Use `PermissionHelperService` for role validation.

**CSRF:** Ensure all state-changing endpoints (POST/PUT/DELETE) are covered by the CSRF filter. No state-changing operation should be CSRF-exempt unless it is the login endpoint.

**Command Injection:** Never pass user input to `Runtime.exec()` or `ProcessBuilder`. Use allowlists for commands. If shell execution is unavoidable, use parameterized APIs (not shell strings).

**Path Traversal:** Canonicalize paths with `Path.normalize()` and verify the resolved path starts with the allowed base directory. Reject `..` sequences.

**Input Validation:** Add `@Valid` on `@RequestBody` parameters. Use `@Pattern`, `@Size`, `@Min`, `@Max` on fields. Sanitize all output to prevent XSS.

**Security Headers:** Add to `SecurityConfig.java`:
```java
.headers(headers -> headers
    .contentTypeOptions(withDefaults())
    .frameOptions(frame -> frame.deny())
    .contentSecurityPolicy(csp -> csp.policyDirectives("default-src 'self'"))
    .httpStrictTransportSecurity(hsts -> hsts.maxAgeInSeconds(31536000))
)
```

**Open Redirect:** Validate redirect URLs against an allowlist of internal paths. Never redirect to user-supplied absolute URLs.

**Deserialization:** Avoid `ObjectInputStream` entirely. Use JSON (Jackson) with `@JsonTypeInfo` disabled or restricted to a closed set of types.

**Dependencies:** Upgrade to latest patch versions. Run `./mvnw dependency-check:check` and `cd Angular && npm audit fix`.

### Workflow for Sub-Agents

When using Claude Code sub-agents for parallel vulnerability auditing:

1. **Audit phase** — Launch parallel Explore agents, one per vulnerability category, targeting the files listed in the table above. Each agent should produce a findings list with: file path, line number, vulnerability description, severity (Critical/High/Medium/Low), and suggested fix.

2. **Fix phase** — Fix vulnerabilities one category at a time in priority order: Critical (SQLi, Command Injection, Deserialization) → High (Access Control, IDOR, Path Traversal) → Medium (CSRF, Headers, Input Validation, Redirect, Info Disclosure) → Low (Dependencies).

3. **Verify phase** — After each category fix: rebuild (`./mvnw clean install`), re-run scans, and confirm the vulnerability is resolved.

### Files Most Likely to Contain Vulnerabilities

**High priority (audit these first):**
- `dao/BugHuntrQueryConstants.java` — all SQL constants (~758 lines)
- `dao/*DAOImpl.java` — all 15+ DAO implementations
- `controller/*API.java` — all REST controllers
- `SecurityConfig.java` — access control rules
- `util/CSRFValidationFilter.java` — CSRF logic
- `controller/frontend.java` — static file serving (path traversal risk)

**Medium priority:**
- `service/*ServiceImpl.java` — business logic layer
- `model/*.java` — deserialization targets
- `Angular/src/app/` — frontend input handling
- `application.properties` — exposed secrets
- `pom.xml` / `package.json` — dependency versions
