package com.griddynamics.internship.userservice.communication.mapper;

import com.griddynamics.internship.userservice.model.user.User;
import com.griddynamics.internship.userservice.model.user.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface FullResponseMapper {
    FullResponseMapper INSTANCE = Mappers.getMapper(FullResponseMapper.class);

    List<UserDTO> usersToDTO(List<User> user);
}
