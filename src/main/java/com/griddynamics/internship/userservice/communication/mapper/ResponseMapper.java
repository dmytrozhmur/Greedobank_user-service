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

@Mapper(componentModel = "spring")
public abstract class ResponseMapper {
    public UserPage usersToDTO(Page<User> user, Pageable pageRequest) {
        return new UserPage(
                user.getContent().stream().map(UserDTO::new).toList(),
                pageRequest,
                user.getTotalElements()
        );
    }


}
