package com.griddynamics.internship.userservice.communication.mapper;

import com.griddynamics.internship.userservice.model.user.User;
import com.griddynamics.internship.userservice.model.user.UserDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserResponseMapper {
    List<UserDTO> usersToDTO(List<User> users);
}
