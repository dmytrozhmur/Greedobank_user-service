package com.griddynamics.internship.userservice.unit;

import com.griddynamics.internship.userservice.communication.request.UserDataRequest;
import com.griddynamics.internship.userservice.exception.NonExistentDataException;
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
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.griddynamics.internship.userservice.utils.ResponseMessages.USER_NOT_FOUND;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    protected final static Role TEST_ADMIN_ROLE = new Role(1, RoleTitle.ROLE_ADMIN);
    protected final static String TEST_EMAIL = "dzhmur@griddynamics.com";
    protected static final int TEST_ID = 0;
    public static final String TEST_PASSWORD = "password";
    public static final int TEST_PAGE = 1;
    public static final int TEST_PAGE_SIZE = 10;
    @MockBean
    protected UserRepository userRepository;
    @Autowired
    protected UserService userService;
    @MockBean
    protected RoleRepository roleRepository;
    protected List<User> mockedUsers;
    protected static PageRequest pageRequest;

    @BeforeEach
    private void prepareData() {
        pageRequest = PageRequest.of(TEST_PAGE - 1, TEST_PAGE_SIZE);

        when(roleRepository.findByTitle(RoleTitle.ROLE_ADMIN)).thenReturn(TEST_ADMIN_ROLE);
        mockedUsers = new ArrayList<>(Arrays.asList(
                new User(
                        TEST_ID,
                        "Dmytro",
                        "Zhmur",
                        TEST_EMAIL,
                        TEST_PASSWORD,
                        roleRepository.findByTitle(RoleTitle.ROLE_ADMIN)
                ),
                new User(
                        1,
                        "Yevheniia",
                        "Komiahina",
                        "ykomiahina@griddynamics.com",
                        TEST_PASSWORD,
                        roleRepository.findByTitle(RoleTitle.ROLE_ADMIN)
                )
        ));
    }

    @Test
    public void getAllUsers() {
        Collection<UserDTO> expected = getAllExpectedUsers();

        when(userRepository
                .findAll(pageRequest))
                .thenReturn(new PageImpl<>(mockedUsers));

        Collection<UserDTO> actual = userService.findAll(TEST_PAGE, TEST_PAGE_SIZE).getContent();

        verify(userRepository).findAll(pageRequest);
        assertThat(actual, is(expected));
    }

    @Test
    public void getUsersByEmail() {
        List<UserDTO> expected = Collections.singletonList(
                new UserDTO(TEST_ID, TEST_EMAIL, TEST_ADMIN_ROLE)
        );

        when(userRepository
                .findAuthorizationInfoByEmail(pageRequest, TEST_EMAIL))
                .thenReturn(new PageImpl<>(mockedUsers.subList(0, 1).stream()
                        .map(user -> new User(
                                user.getId(),
                                user.getEmail(),
                                user.getPassword(),
                                user.getRole()
                        )).toList()));

        List<UserDTO> actual = userService.findAll(TEST_PAGE, TEST_PAGE_SIZE, TEST_EMAIL).getContent();

        verify(userRepository).findAuthorizationInfoByEmail(pageRequest, TEST_EMAIL);
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
                TEST_ADMIN_ROLE
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

    @Test
    public void deleteUserAndCatchExceptionWhenGetItById() {
        when(userRepository.existsById(TEST_ID)).thenReturn(true);
        when(userRepository.getReferenceById(TEST_ID)).thenReturn(mockedUsers.get(0));
        doAnswer((Answer<Object>) invocation -> {
            int id = invocation.getArgument(0);
            when(userRepository.existsById(id)).thenReturn(false);
            return null;
        }).when(userRepository).deleteById(any(Integer.class));

        userService.deleteUser(TEST_ID);
        Exception exception = assertThrows(
                NonExistentDataException.class,
                () -> userService.findUser(TEST_ID)
        );

        assertThat(exception.getMessage(), is(USER_NOT_FOUND));
    }

    protected ArrayList<UserDTO> getAllExpectedUsers() {
        return new ArrayList<>(Arrays.asList(
                new UserDTO(TEST_ID, "Dmytro", "Zhmur", TEST_EMAIL, TEST_ADMIN_ROLE),
                new UserDTO(1, "Yevheniia", "Komiahina", "ykomiahina@griddynamics.com", TEST_ADMIN_ROLE)
        ));
    }
}
