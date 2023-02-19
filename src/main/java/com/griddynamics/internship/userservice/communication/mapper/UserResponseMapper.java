package com.griddynamics.internship.userservice.communication.mapper;

import com.griddynamics.internship.userservice.model.child.ChildDTO;
import com.griddynamics.internship.userservice.model.user.User;
import com.griddynamics.internship.userservice.model.user.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public abstract class UserResponseMapper {
    @Autowired
    private ChildrenResponseMapper childrenMapper;

    public List<UserDTO> usersToDTO(List<User> users) {
        List<UserDTO> mappedUsers = new ArrayList<>();
        for (User user : users) {
            UserDTO userDTO = userToDTO(user);
            List<ChildDTO> childrenDTO = user.getChildren().stream()
                    .map(childrenMapper::childToDTO)
                    .toList();
            userDTO.setChildren(childrenDTO);
            mappedUsers.add(userDTO);
        }
        return mappedUsers;
    }

    @Mapping(target = "children", ignore = true)
    public abstract UserDTO userToDTO(User user);
}
