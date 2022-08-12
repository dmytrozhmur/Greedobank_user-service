package com.griddynamics.internship.userservice.service;

import com.griddynamics.internship.userservice.communication.request.SigninRequest;
import com.griddynamics.internship.userservice.exception.EmailExistsException;
import com.griddynamics.internship.userservice.communication.request.SignupRequest;
import com.griddynamics.internship.userservice.exception.NonExistentDataException;
import com.griddynamics.internship.userservice.model.JwtUser;
import com.griddynamics.internship.userservice.model.Role;
import com.griddynamics.internship.userservice.model.RoleTitle;
import com.griddynamics.internship.userservice.model.User;
import com.griddynamics.internship.userservice.model.UserDTO;
import com.griddynamics.internship.userservice.model.UserWrapper;
import com.griddynamics.internship.userservice.repo.RoleRepository;
import com.griddynamics.internship.userservice.repo.UserRepository;
import com.griddynamics.internship.userservice.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

import static com.griddynamics.internship.userservice.utils.ResponseMessages.EMAIL_IN_USE;

@Service
public class UserService {
    private UserRepository userRepository;
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public Collection<UserDTO> findAll() {
        return userRepository.findAll()
                .stream()
                .map(UserDTO::new)
                .toList();
    }

    public void createUser(SignupRequest signup) {
        if(userRepository.findByEmail(signup.getEmail()) != null)
            throw new EmailExistsException(EMAIL_IN_USE);

        String encodedPassword = passwordEncoder.encode(signup.getPassword());
        Role specifiedRole = signup.getRole();
        Role appropriateRole = specifiedRole
                == null ? roleRepository.findByTitle(RoleTitle.ROLE_USER) : specifiedRole;

        userRepository.save(new User(
                signup.getFirstName(),
                signup.getLastName(),
                signup.getEmail(),
                encodedPassword,
                appropriateRole)
        );
    }

    public JwtUser verifyUser(SigninRequest signin) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(signin.getEmail(), signin.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(auth);
        String token = JwtUtils.generateToken(auth);
        UserWrapper userDetails = (UserWrapper) auth.getPrincipal();

        return new JwtUser(
                token,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getUsername(),
                ((GrantedAuthority) userDetails.getAuthorities().toArray()[0]).getAuthority()
        );
    }
}
