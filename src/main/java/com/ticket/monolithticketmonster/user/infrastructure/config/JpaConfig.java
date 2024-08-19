package com.ticket.monolithticketmonster.user.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Enable Jpa auditing to start auto generate "createdAt" & "updatedAt".
 */
@Configuration
@EnableJpaAuditing
public class JpaConfig {}
