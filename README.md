# Ticket Monster

> A ticket purchase system with Spring Modulith based on Domain-Driven Design (DDD) principles.


## Features

1. **Domain-Driven Design (DDD)**: The codebase is designed following **DDD principles**, ensuring
   maintainability and scalability.
2. **Authentication**: Secures the system using Spring Security, implementing **JWT** and the *
   *OAuth2 PKCE** approach.
3. **Lock Mechanism**: Uses Spring Data JPA for CRUD operations and implements **locking mechanisms
   ** to ensure data consistency and prevent race conditions in ticket operations.
4. **Java Util Concurrency (JUC) for Handling Concurrency**: Leverages Java's concurrency utilities
   to manage high-volume ticket operations effectively.
5. **Pub/Sub Mechanism for Multiple Instances**: Employs Spring Kafka to broadcast ticket status
   updates across multiple instances for real-time synchronization.
6. **WebSocket**: Enables real-time communication and updates to clients via WebSocket connections.
