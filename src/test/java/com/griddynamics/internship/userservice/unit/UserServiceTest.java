package com.griddynamics.internship.userservice.unit;

import com.griddynamics.internship.userservice.communication.request.UserDataRequest;
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
import org.mockito.Answers;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    private final static Role TEST_ROLE = new Role(1, RoleTitle.ROLE_ADMIN);

    private final static String TEST_EMAIL = "dzhmur@griddynamics.com";
    private static final int TEST_ID = 0;
    public static final String TEST_PASSWORD = "password";
    @MockBean
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @MockBean
    private RoleRepository roleRepository;
    private List<User> mockedUsers;

    @BeforeEach
    private void prepareData() {
        when(roleRepository.findByTitle(RoleTitle.defaultTitle())).thenReturn(TEST_ROLE);
        mockedUsers = new ArrayList<>(Arrays.asList(
                new User(
                        TEST_ID,
                        "Dmytro",
                        "Zhmur",
                        TEST_EMAIL,
                        TEST_PASSWORD,
                        roleRepository.findByTitle(RoleTitle.defaultTitle())
                ),
                new User(
                        1,
                        "Yevheniia",
                        "Komiahina",
                        "ykomiahina@griddynamics.com",
                        TEST_PASSWORD,
                        roleRepository.findByTitle(RoleTitle.defaultTitle())
                )
        ));
    }

    @Test
    public void getAllUsers() {
        Collection<UserDTO> expected = new ArrayList<>(Arrays.asList(
                new UserDTO(TEST_ID, "Dmytro", "Zhmur", TEST_EMAIL, TEST_ROLE),
                new UserDTO(1, "Yevheniia", "Komiahina", "ykomiahina@griddynamics.com", TEST_ROLE)
        ));

        when(userRepository.findAll()).thenReturn(mockedUsers);

        Collection<UserDTO> actual = userService.findAll();

        verify(userRepository).findAll();
        assertThat(actual, is(expected));
    }

    @Test
    public void getUsersByEmail() {
        List<UserDTO> expected = Collections.singletonList(
                new UserDTO(TEST_ID, TEST_EMAIL, TEST_ROLE)
        );

        when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(mockedUsers.get(0));

        List<UserDTO> actual = userService.findAll(TEST_EMAIL);

        verify(userRepository).findByEmail(TEST_EMAIL);
        assertThat(actual, is(expected));
    }

    @Test
    public void updateUserAndGetById() {
        String newFirstName = "Grid";
        String newLastName = "Dynamics";
        String newEmail = "dmytro.zhmur@nure.ua";
        UserDTO expected = new UserDTO(new User(
                TEST_ID,
                newFirstName,
                newLastName,
                newEmail,
                TEST_PASSWORD,
                TEST_ROLE
        ));

        when(userRepository.existsById(TEST_ID)).thenReturn(true);
        when(userRepository.getReferenceById(TEST_ID)).thenReturn(mockedUsers.get(0));
        doAnswer((Answer<Object>) invocation -> {
            User user = invocation.getArgument(0);
            when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
            return null;
        }).when(userRepository).save(any(User.class));

        UserDataRequest request = new UserDataRequest();
        request.setFirstName(newFirstName);
        request.setLastName(newLastName);
        request.setEmail(newEmail);
        userService.updateUser(TEST_ID, request);
        UserDTO actual = userService.findUser(TEST_ID);

        assertThat(actual, is(expected));
    }

}
