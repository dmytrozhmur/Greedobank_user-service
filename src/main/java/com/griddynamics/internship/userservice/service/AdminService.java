package com.griddynamics.internship.userservice.service;

import com.griddynamics.internship.userservice.communication.mapper.ResponseMapper;
import com.griddynamics.internship.userservice.communication.response.UserPage;
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
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static com.griddynamics.internship.userservice.utils.PageRequests.generatePageRequest;
import static com.griddynamics.internship.userservice.utils.ResponseMessages.USER_NOT_FOUND;

@Service
public class AdminService {
    @Autowired
    private ResponseMapper mapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private RestTemplate restTemplate;

    public Page<UserDTO> findAll(int pageNum, int pageSize) {
        PageRequest pageRequest = generatePageRequest(pageNum, pageSize);
        Role adminRole = roleRepository.findByTitle(RoleTitle.ROLE_ADMIN);
        Page<User> allAdmins = userRepository.findAllByRole(pageRequest, adminRole);

        return new UserPage(
                mapper.usersToDTO(allAdmins.getContent()),
                pageRequest,
                allAdmins.getTotalElements()
        );
    }

    public UserDTO findAdmin(int id) {
        Optional<User> admin = userRepository.findByIdAndRole(
                id, roleRepository.findByTitle(RoleTitle.ROLE_ADMIN));

        return admin
                .map(UserDTO::new)
                .orElseThrow(() -> new NonExistentDataException(USER_NOT_FOUND));
    }

    public Page<UserDTO> findCardTemplates(int id) {
        return null;
    }
}
