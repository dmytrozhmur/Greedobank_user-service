package com.griddynamics.internship.userservice.repo;

import com.griddynamics.internship.userservice.model.child.ChildAccount;
import com.griddynamics.internship.userservice.model.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChildrenRepository extends JpaRepository<ChildAccount, Integer> {
    Page<ChildAccount> findAllByParentsContains(Pageable pageRequest, User parent);
}
