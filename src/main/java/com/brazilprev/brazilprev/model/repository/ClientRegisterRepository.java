package com.brazilprev.brazilprev.model.repository;

import com.brazilprev.brazilprev.model.domain.ClientRegister;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRegisterRepository extends CustomJpaRepository<ClientRegister, Long> {

    Optional<ClientRegister> findByCpf(String cpf);

    @Query("from ClientRegister c join fetch c.address join fetch c.address.city a join fetch a.state")
    List<ClientRegister> findAll();

    @Query("from ClientRegister c join fetch c.address join fetch c.address.city a join fetch a.state WHERE c.active = true")
    List<ClientRegister> findAllActives();

}
