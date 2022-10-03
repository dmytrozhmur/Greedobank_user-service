package com.griddynamics.internship.userservice.service;

import com.griddynamics.internship.userservice.component.holder.UserProps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PageRequestService {
    @Value("${user-service.users.page-size}")
    private int usersPerPage;
    @Value("${user-service.users.start-page}")
    private int startPage;

    public PageRequest generatePageRequest(Optional<Integer> page) {
        int pageNumber = page.orElse(startPage) - 1;
        return PageRequest.of(pageNumber, usersPerPage);
    }
}
