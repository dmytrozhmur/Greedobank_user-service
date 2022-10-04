package com.griddynamics.internship.userservice.communication.mapper;

import com.fasterxml.jackson.databind.JsonNode;
import com.griddynamics.internship.userservice.communication.response.UserPage;
import com.griddynamics.internship.userservice.model.user.User;
import com.griddynamics.internship.userservice.model.user.UserDTO;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ResponseMapper {
    List<UserDTO> usersToDTO(List<User> users);
}
