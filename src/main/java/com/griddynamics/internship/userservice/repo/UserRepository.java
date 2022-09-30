package com.griddynamics.internship.userservice.repo;

import com.griddynamics.internship.userservice.model.user.User;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Integer> {
    Page<User> findAll(Pageable pageRequest);
    @Query("select new com.griddynamics.internship.userservice.model.user.User(" +
            "u.id, u.email, u.password, u.role) " +
            "from user u " +
            "where u.email = ?1")
    Page<User> findByEmail(Pageable pageRequest, String email);
    boolean existsByEmail(@NonNull String email);
}
