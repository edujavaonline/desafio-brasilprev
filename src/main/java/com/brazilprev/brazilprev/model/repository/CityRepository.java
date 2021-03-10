package com.brazilprev.brazilprev.model.repository;

import com.brazilprev.brazilprev.model.domain.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {

    @Query("from City c join fetch c.state WHERE c.state.id = :stateId")
    List<City> findByStateId(Long stateId);
}
