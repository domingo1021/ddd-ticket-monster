package com.ticket.monolithticketmonster.user.domain;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, UserId> {
  Optional<User> findByEmail(Email email);
}
