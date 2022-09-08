package com.griddynamics.internship.userservice.communication.mapper;

import com.griddynamics.internship.userservice.communication.request.UserDataRequest;
import com.griddynamics.internship.userservice.model.role.Role;
import com.griddynamics.internship.userservice.model.user.User;
import com.griddynamics.internship.userservice.repo.RoleRepository;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static com.griddynamics.internship.userservice.model.role.RoleTitle.defaultTitle;

//@Component
@Mapper(componentModel = "spring")
public abstract class FullRequestMapper {
    //public static FullRequestMapper INSTANCE = Mappers.getMapper(FullRequestMapper.class);
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository roleRepository;

    public User requestToUser(UserDataRequest request) {

        Role specifiedRole = request.getRole();
        Role appropriateRole = specifiedRole
                == null ? roleRepository.findByTitle(defaultTitle()) : specifiedRole;
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        return new User(
                request.getFirstName(),
                request.getLastName(),
                request.getEmail(),
                encodedPassword,
                appropriateRole
        );
    }
}
