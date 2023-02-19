package com.griddynamics.internship.userservice.repo;

import com.griddynamics.internship.userservice.model.role.Role;
import com.griddynamics.internship.userservice.model.user.User;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Page<User> findAll(Pageable pageRequest);
    @Query("select new com.griddynamics.internship.userservice.model.user.User(" +
            "u.id, u.email, u.password, u.role) " +
            "from user u " +
            "where u.email = ?1")
    Page<User> findAuthorizationInfoByEmail(Pageable pageRequest, String email);
    Page<User> findAllByRole(Pageable pageRequest, Role role);
    Optional<User> findByIdAndRole(int id, Role role);
    boolean existsByIdAndRole(int id, Role role);
    boolean existsByEmail(@NonNull String email);
    boolean existsByIdIn(List<Integer> ids);
}
