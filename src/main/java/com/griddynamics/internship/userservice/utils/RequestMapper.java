package com.griddynamics.internship.userservice.utils;

import com.griddynamics.internship.userservice.communication.request.UserDataRequest;
import com.griddynamics.internship.userservice.model.user.User;

public class RequestMapper {
    public static void toUser(UserDataRequest request, User user) {
        String firstName = request.getFirstName();
        if(firstName != null) user.setFirstName(firstName);

        String lastName = request.getLastName();
        if(lastName != null) user.setLastName(lastName);

        String password = request.getPassword();
        if(password != null) user.setPassword(password);

        String email = request.getEmail();
        if(email != null) user.setEmail(email);
    }
}
