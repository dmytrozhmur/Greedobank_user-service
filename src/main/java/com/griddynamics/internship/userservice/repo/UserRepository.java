package com.griddynamics.internship.userservice.repo;

import com.griddynamics.internship.userservice.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByEmail(String email);
}
