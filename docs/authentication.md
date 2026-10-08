# Authentication Design

This document defines the authentication and authorization design for the Shipment Disruption Risk API.

## 1. Authentication Flow Overview

When a user submits a valid username and password, the system authenticates them, issues a JSON Web Token (JWT), and returns it to the client. The client uses this token on every subsequent request to access protected endpoints. The system determines what the user can do based on their role, which is loaded from the database on each request.

If the credentials are invalid, the system returns a 401 response with a single generic error message. The same message is returned whether the username does not exist or the password is incorrect, to prevent attackers from enumerating valid usernames.

## 2. Endpoint Access Matrix

| Endpoint | Method | Public or Protected | Required Role |
|----------|--------|---------------------|---------------|
| `/api/auth/login` | POST | Public | — |
| `/actuator/health` | GET | Public | — |
| `/api/users/me` | GET | Protected | Any authenticated user |
| `/api/shipments` | GET | Protected | USER, OPERATIONS_MANAGER, ADMIN |
| `/api/shipments/{id}` | GET | Protected | USER, OPERATIONS_MANAGER, ADMIN |
| `/api/shipments/{id}/risk` | GET | Protected | USER, OPERATIONS_MANAGER, ADMIN |
| `/api/shipments/{id}/review` | POST | Protected | OPERATIONS_MANAGER, ADMIN |
| `/api/analytics/**` | GET | Protected | OPERATIONS_MANAGER, ADMIN |
| `/api/admin/users/**` | Any | Protected | ADMIN |

This matrix will be updated as new endpoints are added.

## 3. Login Request and Response Contract

### Request

```
POST /api/auth/login
Content-Type: application/json
```

```json
{
  "username": "abc",
  "password": "abc#123D"
}
```

### Success Response

```
HTTP 200 OK
```

```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "username": "abc",
  "role": "USER"
}
```

### Failure Response

```
HTTP 401 Unauthorized
```

```json
{
  "error": "Invalid username or password"
}
```

The same message is returned whether the username does not exist or the password is incorrect.

## 4. JWT Claims

| Claim | Type | Meaning |
|-------|------|---------|
| `sub` | String | Username of the authenticated user |
| `role` | String | USER, OPERATIONS_MANAGER, or ADMIN |
| `iat` | Date | Issued at |
| `exp` | Date | Expiration (1 hour after issuance) |

The token contains no user ID, email, password, or other sensitive data. Claims are base64-encoded, not encrypted, so anyone with the token can read them. The signature prevents tampering, not reading.

## 5. Token Generation and Validation Strategy

**Generation.** A `JwtService` generates tokens using the JJWT library. The signing algorithm is HMAC-SHA256, which requires a secret of at least 32 bytes. The secret is loaded from the `JWT_SECRET` environment variable. The default value in `application.properties` is a development placeholder and must never be used in production.

**Lifetime.** Tokens expire 1 hour after issuance. This limits the impact of a stolen token while keeping the user session reasonable.

**Validation.** On every authenticated request, the `JwtAuthenticationFilter` performs the following steps:

1. Reads the `Authorization` header. If it does not start with `Bearer `, the request continues unauthenticated.
2. Extracts the token and validates its signature and expiration.
3. Extracts the username from the `sub` claim.
4. Loads the user from the database via `UserRepository.findByUsername`.
5. If the user does not exist, is inactive, or the token is invalid, the request is rejected with 401.
6. If the user exists and is active, the `SecurityContext` is populated with the user's current role.

**Why database lookup.** Immediate enforcement of role changes and deactivations is a requirement. A purely stateless design would leave a window during which stale claims remain valid. Given that PostgreSQL is the primary data store and lookups by indexed username are fast at this scale, database lookup is the correct trade-off. It is simpler than refresh tokens or a Redis-backed token store and provides the same enforcement guarantee.

**Performance consideration.** At high request volumes, the per-request database lookup would become a bottleneck. The mitigation would be a Redis cache keyed by username with explicit invalidation on user updates. This is documented as future work and is not implemented in version 1.

## 6. SecurityConfig Rules

The `SecurityConfig` class will be updated as follows:

- Disable CSRF protection (this is a stateless API, not a browser-session application).
- Set session management to `STATELESS`.
- Permit `POST /api/auth/login` without authentication.
- Permit `/actuator/health` without authentication (used by deployment health checks).
- Require authentication for every other endpoint.
- Restrict `/api/admin/**` to users with the `ADMIN` role.
- Register the `JwtAuthenticationFilter` before Spring Security's `UsernamePasswordAuthenticationFilter`.
- Enable method-level security with `@EnableMethodSecurity` so controllers can use `@PreAuthorize` for finer-grained rules.

## 7. Test Plan

| Test class | Type | What it verifies |
|------------|------|------------------|
| `JwtServiceTest` | Unit | Token generation for a given username and role; username extraction; role extraction; rejection of tampered tokens; rejection of expired tokens. |
| `AuthControllerTest` | Integration (`@WebMvcTest`) | Login with valid credentials returns 200 and a token; login with an unknown username returns 401; login with an incorrect password returns 401; both failure cases return the same message. |
| `JwtAuthenticationFilterTest` | Unit | Valid token populates the `SecurityContext`; invalid token leaves it empty; expired token leaves it empty; token for a deactivated user leaves it empty. |

## 8. Out of Scope for Version 1

The following are documented as future work and are not implemented in version 1:

- Refresh tokens with server-side revocation.
- Token blacklisting in Redis.
- Token versioning on the `User` entity.
- Multi-factor authentication.
- Password reset flow.
- Email or in-app notifications when a user's role changes.

If any of these become requirements, they should be added to this document before implementation.
