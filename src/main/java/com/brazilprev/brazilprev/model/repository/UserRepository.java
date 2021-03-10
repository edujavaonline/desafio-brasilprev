package com.brazilprev.brazilprev.model.repository;

import com.brazilprev.brazilprev.model.domain.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CustomJpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
}
