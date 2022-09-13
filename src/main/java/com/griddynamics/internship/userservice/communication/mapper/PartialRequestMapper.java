package com.griddynamics.internship.userservice.communication.mapper;

import com.griddynamics.internship.userservice.communication.request.UserDataRequest;
import com.griddynamics.internship.userservice.model.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PartialRequestMapper {
    @Mapping(target = "firstName")
    @Mapping(target = "lastName")
    @Mapping(target = "email")
    @Mapping(target = "password")
    @Mapping(target = "role")
    void requestToUser(UserDataRequest request, @MappingTarget User user);
}
