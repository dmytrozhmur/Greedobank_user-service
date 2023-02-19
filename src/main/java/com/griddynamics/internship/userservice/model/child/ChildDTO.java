package com.griddynamics.internship.userservice.model.child;

import com.griddynamics.internship.userservice.model.user.User;
import com.griddynamics.internship.userservice.model.user.UserDTO;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
public class ChildDTO {
    private Integer id;
    private String login;
    private String contactDetails;
    private Instant createdAt;
    private List<UserDTO> parents;
}
