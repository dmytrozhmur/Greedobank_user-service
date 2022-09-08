package com.griddynamics.internship.userservice.communication.mapper;

import com.griddynamics.internship.userservice.communication.request.UserDataRequest;
import com.griddynamics.internship.userservice.model.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface RequestMapper {
    RequestMapper INSTANCE = Mappers.getMapper(RequestMapper.class);

    @Mapping(source = "request.firstName", target = "firstName",
            defaultExpression = "java(user.getFirstName())")
    @Mapping(source = "request.lastName", target = "lastName",
            defaultExpression = "java(user.getLastName())")
    @Mapping(source = "request.email", target = "email",
            defaultExpression = "java(user.getEmail())")
    @Mapping(source = "request.password", target = "password",
            defaultExpression = "java(user.getPassword())")
    @Mapping(source = "request.role", target = "role",
            defaultExpression = "java(user.getRole())")
    User requestToUser(UserDataRequest request, User user);
}
