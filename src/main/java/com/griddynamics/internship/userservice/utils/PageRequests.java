package com.griddynamics.internship.userservice.utils;

import org.springframework.data.domain.PageRequest;

public class PageRequests {
    public static PageRequest generatePageRequest(int pageNumber, int usersPerPage) {
        return PageRequest.of(pageNumber - 1, usersPerPage);
    }
}
