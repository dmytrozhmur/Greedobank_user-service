package com.griddynamics.internship.userservice.unit;

import com.griddynamics.internship.userservice.communication.request.UserDataRequest;
import com.griddynamics.internship.userservice.communication.response.JsonResponse;
import com.griddynamics.internship.userservice.model.role.Role;
import com.griddynamics.internship.userservice.model.role.RoleTitle;
import com.griddynamics.internship.userservice.model.user.UserWrapper;
import com.griddynamics.internship.userservice.controller.UserController;
import com.griddynamics.internship.userservice.model.user.User;
import com.griddynamics.internship.userservice.model.user.UserDTO;
import com.griddynamics.internship.userservice.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;

import javax.validation.ConstraintViolationException;
import java.util.Optional;

import static com.griddynamics.internship.userservice.utils.ResponseMessages.FAILURE;
import static com.griddynamics.internship.userservice.utils.ResponseMessages.INAPPROPRIATE_SIZE;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@Sql(value = "insert_first_users.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = "clear_database.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class UserControllerTest {
    private static final int TEST_ID = 1;
    private static final String NEW_LASTNAME = "Zibrov";
    private static final String NEW_EMAIL = "dzibrov@griddynamics.com";
    private UserWrapper authUser = new UserWrapper(new User());
    private UserDataRequest request = new UserDataRequest();

    @Autowired
    private UserController userController;
    @MockBean
    private UserService userService;

    @Test
    @WithUserDetails("dzhmur@griddynamics.com")
    public void updateAccountAndFindItSuccess() {
        UserDTO expected = new UserDTO(
                TEST_ID,
                "Dmytro",
                NEW_LASTNAME,
                NEW_EMAIL,
                new Role(1, RoleTitle.ROLE_ADMIN)
        );
        doAnswer((Answer<Object>) invocation -> {
            int id = invocation.getArgument(0);
            UserDataRequest request = invocation.getArgument(1);
            when(userService.findUser(id)).thenReturn(new UserDTO(
                    id,
                    expected.getFirstName(),
                    request.getLastName(),
                    request.getEmail(),
                    expected.getRole()
            ));
            return null;
        }).when(userService).updateUser(any(Integer.class), any(UserDataRequest.class));

        request.setLastName(NEW_LASTNAME);
        request.setEmail(NEW_EMAIL);
        userController.updateAccount(
                authUser,
                request,
                TEST_ID
        );
        UserDTO actual = userController.getUserInfo(authUser, TEST_ID).getContent();

        assertThat(actual, is(expected));
    }

    @Test
    @WithUserDetails("tkomarova@griddynamics.com")
    public void updateAccountAccessDenied() {
        Exception exception = assertThrows(
                AccessDeniedException.class, () -> userController.updateAccount(
                        authUser,
                        request,
                        TEST_ID
                )
        );
        assertThat(exception.getMessage(), is("Access is denied"));
    }

    @Test
    @WithUserDetails("dzhmur@griddynamics.com")
    public void updateAccountInvalidField() {
        UserDataRequest wrongRequest = new UserDataRequest("", "", "", "");
        ConstraintViolationException exception = assertThrows(
                ConstraintViolationException.class,
                () -> userController.updateAccount(
                        authUser,
                        wrongRequest,
                        TEST_ID
                )
        );

        String message = exception.getMessage();
        assertThat(message, containsString("updateAccount.userDataRequest.password: length must consists of 8 - 20 characters"));
        assertThat(message, containsString("updateAccount.userDataRequest.lastName: must contains from 1 till 45 characters"));
        assertThat(message, containsString("updateAccount.userDataRequest.email: incorrect format"));
        assertThat(message, containsString("updateAccount.userDataRequest.firstName: must contains from 1 till 45 characters"));
        assertThat(message, containsString("updateAccount.userDataRequest.email: must contains from 1 till 45 characters"));
    }
}