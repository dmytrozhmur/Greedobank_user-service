package com.griddynamics.internship.userservice.repo;

import com.griddynamics.internship.userservice.model.token.Refreshment;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RefreshmentRepository extends CrudRepository<Refreshment, Long> {
    Optional<Refreshment> findByToken(String token);
}
