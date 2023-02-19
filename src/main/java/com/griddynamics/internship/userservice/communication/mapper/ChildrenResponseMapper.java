package com.griddynamics.internship.userservice.communication.mapper;

import com.griddynamics.internship.userservice.model.child.ChildAccount;
import com.griddynamics.internship.userservice.model.child.ChildDTO;
import com.griddynamics.internship.userservice.model.user.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public abstract class ChildrenResponseMapper {
    @Lazy
    @Autowired
    private UserResponseMapper userMapper;

    public List<ChildDTO> childrenToDTO(List<ChildAccount> children) {
        List<ChildDTO> mappedUsers = new ArrayList<>();
        for (ChildAccount child : children) {
            ChildDTO childDTO = childToDTO(child);
            List<UserDTO> parentsDTO = child.getParents().stream()
                    .map(userMapper::userToDTO)
                    .toList();
            childDTO.setParents(parentsDTO);
            mappedUsers.add(childDTO);
        }
        return mappedUsers;
    }

    @Mapping(target = "parents", ignore = true)
    public abstract ChildDTO childToDTO(ChildAccount child);
}
