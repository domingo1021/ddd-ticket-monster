# Ticket Monster (In Development)

> This repository is a Mono-Repo created to practice microservices architecture using Spring Boot
> and Spring Cloud.

## Core Services

- [ ] **API Gateway**
- [x] **Auth Service**
- [x] **Ticket Service**

## Features

### API Gateway

1. **API Proxy**: Routes requests to the appropriate services based on defined paths and prefixes.
2. **Authentication**: Integrates with the Auth Service to enforce security policies and validate
   JWT tokens for specific endpoints.
3. **Filtering**: Allows pre- and post-processing of requests, including logging, header
   manipulation, and more.
4. **Rate Limiting**: Implements a Token Bucket rate limiter to control the rate of incoming
   requests and prevent abuse.
5. **Access Control List (ACL)**: Enforces fine-grained access control to specific endpoints and
   resources.

### Authentication Service

1. **OAuth2**: Provides an OAuth2 server to handle authorization and secure access to resources.
2. **JWT**: Issues JSON Web Tokens (JWT) for secure and stateless authentication.
3. **Normal Sign Up / Sign In**: Supports traditional user registration and login, with secure
   password handling.

### Ticket Service

1. **Java Util Concurrency (JUC) to Handle Concurrency**: Utilizes Java's concurrency utilities to
   manage high-volume ticket operations.
2. **Lock Mechanism**: Implements locking strategies to ensure data consistency and prevent race
   conditions in ticket operations.
3. **Pub/Sub Mechanism Between Multiple Instances**: Uses Spring Kafka to broadcast ticket status
   updates across multiple instances for real-time synchronization.
4. **WebSocket**: Provides real-time communication and updates to clients via WebSocket connections.
