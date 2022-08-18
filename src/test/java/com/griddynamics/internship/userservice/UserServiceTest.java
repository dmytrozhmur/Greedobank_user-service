package com.griddynamics.internship.userservice;

import com.griddynamics.internship.userservice.model.Role;
import com.griddynamics.internship.userservice.model.RoleTitle;
import com.griddynamics.internship.userservice.model.User;
import com.griddynamics.internship.userservice.model.UserDTO;
import com.griddynamics.internship.userservice.datasource.repo.RoleRepository;
import com.griddynamics.internship.userservice.datasource.repo.UserRepository;
import com.griddynamics.internship.userservice.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    private final static Role TEST_ROLE = new Role(1, RoleTitle.ROLE_ADMIN);
    @MockBean
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @MockBean
    private RoleRepository roleRepository;

    @Test
    public void getAllUsers() {
        Collection<UserDTO> expected = getExpected();

        when(roleRepository.findByTitle(RoleTitle.ROLE_ADMIN)).thenReturn(TEST_ROLE);
        List<User> mockedUsers = new ArrayList<>(Arrays.asList(
                new User(
                        "Dmytro",
                        "Zhmur",
                        "dzhmur@griddynamics.com",
                        "password",
                        roleRepository.findByTitle(RoleTitle.ROLE_ADMIN)
                ),
                new User(
                        "Yevheniia",
                        "Komiahina",
                        "ykomiahina@griddynamics.com",
                        "password",
                        roleRepository.findByTitle(RoleTitle.ROLE_ADMIN)
                )
        ));
        when(userRepository.findAll()).thenReturn(mockedUsers);

        Collection<UserDTO> actual = userService.findAll();

        verify(userRepository).findAll();
        assertThat(actual, is(expected));
    }

    private Collection<UserDTO> getExpected() {
        return new ArrayList<>(Arrays.asList(
                new UserDTO(135, "Dmytro", "Zhmur", "dzhmur@griddynamics.com", TEST_ROLE),
                new UserDTO(87, "Yevheniia", "Komiahina", "ykomiahina@griddynamics.com", TEST_ROLE)
        ));
    }
}
