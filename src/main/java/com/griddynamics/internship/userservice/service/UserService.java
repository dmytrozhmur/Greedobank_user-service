package com.griddynamics.internship.userservice.service;

import com.griddynamics.internship.userservice.communication.mapper.FullRequestMapper;
import com.griddynamics.internship.userservice.communication.mapper.ResponseMapper;
import com.griddynamics.internship.userservice.communication.mapper.PartialRequestMapper;
import com.griddynamics.internship.userservice.communication.request.SigninRequest;
import com.griddynamics.internship.userservice.communication.response.UserPage;
import com.griddynamics.internship.userservice.component.holder.UserProps;
import com.griddynamics.internship.userservice.exception.EmailExistsException;
import com.griddynamics.internship.userservice.communication.request.UserDataRequest;
import com.griddynamics.internship.userservice.exception.NonExistentDataException;
import com.griddynamics.internship.userservice.model.token.Refreshment;
import com.griddynamics.internship.userservice.model.user.JwtUser;
import com.griddynamics.internship.userservice.model.user.User;
import com.griddynamics.internship.userservice.model.user.UserDTO;
import com.griddynamics.internship.userservice.model.user.UserWrapper;
import com.griddynamics.internship.userservice.repo.RoleRepository;
import com.griddynamics.internship.userservice.repo.UserRepository;
import com.griddynamics.internship.userservice.component.processor.JwtProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import static com.griddynamics.internship.userservice.utils.ResponseMessages.EMAIL_IN_USE;
import static com.griddynamics.internship.userservice.utils.ResponseMessages.PAGE_NOT_FOUND;
import static com.griddynamics.internship.userservice.utils.ResponseMessages.USER_NOT_FOUND;

@Service
public class UserService {
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    @Autowired
    private RefreshmentService refreshmentService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private PartialRequestMapper updateMapper;
    @Autowired
    private FullRequestMapper createMapper;
    @Autowired
    private ResponseMapper responseMapper;
    @Autowired
    private JwtProcessor jwtProcessor;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public UserPage findAll(int pageNum, int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNum - 1, pageSize);
        Page<User> users = userRepository.findAll(pageRequest);

        return new UserPage(
                responseMapper.usersToDTO(users.getContent()),
                pageRequest,
                users.getTotalElements()
        );
    }

    public UserPage findAll(int pageNum, int pageSize, String email) {
        PageRequest pageRequest = PageRequest.of(pageNum - 1, pageSize);
        Page<User> user = userRepository.findByEmail(pageRequest, email);
        
        if(user.getNumberOfElements() == 0 && user.getTotalPages() > 0)
            throw new NonExistentDataException(PAGE_NOT_FOUND);
        else if(user.getTotalPages() == 0)
            throw new NonExistentDataException(USER_NOT_FOUND);
        
        return new UserPage(
                responseMapper.usersToDTO(user.getContent()),
                pageRequest,
                user.getTotalElements()
        );
    }

    public UserDTO findUser(int id) {
        return new UserDTO(userRepository
                .findById(id)
                .orElseThrow(() -> new NonExistentDataException(USER_NOT_FOUND)));
    }

    public void createUser(UserDataRequest signup) {
        checkEmail(signup);
        userRepository.save(createMapper.requestToUser(signup));
    }

    public JwtUser verifyUser(SigninRequest signin) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(signin.getEmail(), signin.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(auth);
        String token = jwtProcessor.generateToken((UserWrapper) auth.getPrincipal());
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

    public void updateUser(int userId, UserDataRequest userDataRequest) {
        if(!userRepository.existsById(userId)) throw new NonExistentDataException("User not found");
        checkEmail(userDataRequest);

        User updatedUser = userRepository.getReferenceById(userId);
        updateMapper.requestToUser(userDataRequest, updatedUser);

        userRepository.save(updatedUser);
    }

    public void deleteUser(int userId) {
        userRepository.deleteById(userId);
    }

    private void checkEmail(UserDataRequest signup) {
        if(userRepository.existsByEmail(signup.getEmail()))
            throw new EmailExistsException(EMAIL_IN_USE);
    }
}
