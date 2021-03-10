package com.brazilprev.brazilprev.model.repository;

import com.brazilprev.brazilprev.model.domain.State;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StateRepository extends JpaRepository<State, Long> {
}
