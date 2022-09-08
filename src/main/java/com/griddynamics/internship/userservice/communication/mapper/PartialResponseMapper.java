package com.griddynamics.internship.userservice.communication.mapper;

import com.griddynamics.internship.userservice.model.user.User;
import com.griddynamics.internship.userservice.model.user.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PartialResponseMapper {
    PartialResponseMapper INSTANCE = Mappers.getMapper(PartialResponseMapper.class);

    @Mapping(target = "firstName", ignore = true)
    @Mapping(target = "lastName", ignore = true)
    UserDTO userToDTO(User user);
}
