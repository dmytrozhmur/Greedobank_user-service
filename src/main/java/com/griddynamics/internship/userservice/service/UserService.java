package com.griddynamics.internship.userservice.service;

import com.griddynamics.internship.userservice.communication.request.SigninRequest;
import com.griddynamics.internship.userservice.exception.EmailExistsException;
import com.griddynamics.internship.userservice.communication.request.SignupRequest;
import com.griddynamics.internship.userservice.exception.NonExistentDataException;
import com.griddynamics.internship.userservice.model.token.Refreshment;
import com.griddynamics.internship.userservice.model.user.JwtUser;
import com.griddynamics.internship.userservice.model.role.Role;
import com.griddynamics.internship.userservice.model.user.User;
import com.griddynamics.internship.userservice.model.user.UserDTO;
import com.griddynamics.internship.userservice.model.user.UserWrapper;
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
import java.util.Collections;

import static com.griddynamics.internship.userservice.model.role.RoleTitle.defaultTitle;
import static com.griddynamics.internship.userservice.utils.ResponseMessages.EMAIL_IN_USE;
import static com.griddynamics.internship.userservice.utils.ResponseMessages.USER_NOT_FOUND;

@Service
public class UserService {
    private UserRepository userRepository;
    private RoleRepository roleRepository;

    @Autowired
    private RefreshmentService refreshmentService;
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

    public Collection<UserDTO> findAll(String email) {
        User user = userRepository.findByEmail(email);
        if(user == null) throw new NonExistentDataException(USER_NOT_FOUND);

        return Collections.singletonList(new UserDTO(
                user.getId(),
                user.getEmail(),
                user.getRole()
        ));
    }

    public UserDTO findUser(int id) {
        return new UserDTO(userRepository
                .findById(id)
                .orElseThrow(() -> new NonExistentDataException(USER_NOT_FOUND)));
    }

    public void createUser(SignupRequest signup) {
        if(userRepository.findByEmail(signup.getEmail()) != null)
            throw new EmailExistsException(EMAIL_IN_USE);

        String encodedPassword = passwordEncoder.encode(signup.getPassword());
        Role specifiedRole = signup.getRole();
        Role appropriateRole = specifiedRole
                == null ? roleRepository.findByTitle(defaultTitle()) : specifiedRole;

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
        String token = JwtUtils.generateToken((UserWrapper) auth.getPrincipal());
        UserWrapper userDetails = (UserWrapper) auth.getPrincipal();

        String email = userDetails.getUsername();
        Refreshment refreshment = refreshmentService.generateRefreshToken(email);

        return new JwtUser(
                token,
                refreshment.getToken(),
                userDetails.getId(),
                email,
                email,
                ((GrantedAuthority) userDetails.getAuthorities().toArray()[0]).getAuthority()
        );
    }
}
