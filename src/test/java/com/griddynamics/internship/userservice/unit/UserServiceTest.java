package com.griddynamics.internship.userservice.unit;

import com.griddynamics.internship.userservice.model.role.Role;
import com.griddynamics.internship.userservice.model.role.RoleTitle;
import com.griddynamics.internship.userservice.model.user.User;
import com.griddynamics.internship.userservice.model.user.UserDTO;
import com.griddynamics.internship.userservice.repo.RoleRepository;
import com.griddynamics.internship.userservice.repo.UserRepository;
import com.griddynamics.internship.userservice.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    private final static Role TEST_ROLE = new Role(1, RoleTitle.ROLE_ADMIN);
    private final static String TEST_EMAIL = "dzhmur@griddynamics.com";
    private static final int TEST_ID = 0;
    @MockBean
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @MockBean
    private RoleRepository roleRepository;
    private List<User> mockedUsers;

    @BeforeEach
    private void initRepositories() {
        when(roleRepository.findByTitle(RoleTitle.ROLE_ADMIN)).thenReturn(TEST_ROLE);
        mockedUsers = new ArrayList<>(Arrays.asList(
                new User(
                        TEST_ID,
                        "Dmytro",
                        "Zhmur",
                        TEST_EMAIL,
                        "password",
                        roleRepository.findByTitle(RoleTitle.ROLE_ADMIN)
                ),
                new User(
                        1,
                        "Yevheniia",
                        "Komiahina",
                        "ykomiahina@griddynamics.com",
                        "password",
                        roleRepository.findByTitle(RoleTitle.ROLE_ADMIN)
                )
        ));
        when(userRepository.findAll()).thenReturn(mockedUsers);
        when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(mockedUsers.get(0));
    }

    @Test
    public void getAllUsers() {
        Collection<UserDTO> expected = getAllExpectedUsers();

        Collection<UserDTO> actual = userService.findAll();

        verify(userRepository).findAll();
        assertThat(actual, is(expected));
    }

    @Test
    public void getUsersByEmail() {
        List<UserDTO> expected = Collections.singletonList(
                new UserDTO(TEST_ID, TEST_EMAIL, TEST_ROLE)
        );

        List<UserDTO> actual = userService.findAll(TEST_EMAIL);

        verify(userRepository).findByEmail(TEST_EMAIL);
        assertThat(actual, is(expected));
    }

    private Collection<UserDTO> getAllExpectedUsers() {
        return new ArrayList<>(Arrays.asList(
                new UserDTO(0, "Dmytro", "Zhmur", "dzhmur@griddynamics.com", TEST_ROLE),
                new UserDTO(1, "Yevheniia", "Komiahina", "ykomiahina@griddynamics.com", TEST_ROLE)
        ));
    }
}
