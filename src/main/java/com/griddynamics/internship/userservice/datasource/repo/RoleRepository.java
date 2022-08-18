package com.griddynamics.internship.userservice.datasource.repo;

import com.griddynamics.internship.userservice.model.Role;
import com.griddynamics.internship.userservice.model.RoleTitle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByTitle(RoleTitle title);
}
