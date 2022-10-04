package com.griddynamics.internship.userservice.utils;

import com.griddynamics.internship.userservice.exception.NonExistentDataException;
import org.springframework.data.domain.PageRequest;

import static com.griddynamics.internship.userservice.utils.ResponseMessages.INAPPROPRIATE_PAGE_PARAMS;

public class PageRequests {
    public static PageRequest generatePageRequest(int pageNumber, int usersPerPage) {
        return PageRequest.of(pageNumber - 1, usersPerPage);
    }

    public static void checkPageParams(int page, int size) {
        if(page < 1 || size < 1)
            throw new NonExistentDataException(INAPPROPRIATE_PAGE_PARAMS);
    }
}
