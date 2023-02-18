package com.griddynamics.internship.userservice.communication.mapper;

import com.griddynamics.internship.userservice.model.user.User;
import com.griddynamics.internship.userservice.model.user.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class UserResponseMapper {
    public abstract List<UserDTO> usersToDTO(List<User> users);

    @Mapping(target = "children", ignore = true)
    public abstract UserDTO userToDTO(User user);
}
