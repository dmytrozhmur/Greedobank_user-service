package com.griddynamics.internship.userservice.service;

import com.griddynamics.internship.userservice.communication.mapper.ResponseMapper;
import com.griddynamics.internship.userservice.component.holder.UserProps;
import com.griddynamics.internship.userservice.exception.NonExistentDataException;
import com.griddynamics.internship.userservice.model.role.Role;
import com.griddynamics.internship.userservice.model.role.RoleTitle;
import com.griddynamics.internship.userservice.model.user.User;
import com.griddynamics.internship.userservice.model.user.UserDTO;
import com.griddynamics.internship.userservice.repo.RoleRepository;
import com.griddynamics.internship.userservice.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.griddynamics.internship.userservice.utils.ResponseMessages.USER_NOT_FOUND;

@Service
public class AdminService {
    @Autowired
    private PageRequestService pageService;
    @Autowired
    private ResponseMapper mapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    public Page<UserDTO> findAll(Optional<Integer> page) {
        PageRequest pageRequest = pageService.generatePageRequest(page);
        Role adminRole = roleRepository.findByTitle(RoleTitle.ROLE_ADMIN);
        Page<User> allAdmins = userRepository.findAllByRole(pageRequest, adminRole);

        return mapper.usersToDTO(allAdmins,
                                 pageRequest.getPageNumber(),
                                 pageRequest.getPageSize());
    }

    public UserDTO findAdmin(int id) {
        Optional<User> admin = userRepository.findByIdAndRole(
                id, roleRepository.findByTitle(RoleTitle.ROLE_ADMIN));

        return admin
                .map(UserDTO::new)
                .orElseThrow(() -> new NonExistentDataException(USER_NOT_FOUND));
    }
}
