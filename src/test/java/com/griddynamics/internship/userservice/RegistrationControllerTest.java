package com.griddynamics.internship.userservice;

import com.griddynamics.internship.userservice.controller.request.SignupRequest;
import com.griddynamics.internship.userservice.model.User;
import com.griddynamics.internship.userservice.model.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

import static com.griddynamics.internship.userservice.utils.Response.*;
import static org.assertj.core.api.Assertions.assertThat;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RegistrationControllerTest {
    private static final String TEST_FIRST_NAME = "Dmytro";
    public static final String TEST_LAST_NAME = "Zhmur";
    private static final String TEST_EMAIL = "dmytro.zhmur@nure.ua";
    private static final String TEST_PASSWORD = "password";
    public static final String USED_EMAIL = "dzhmur@griddynamics.com";
    private static final User EXISTING_ENTITY = new User(
            new SignupRequest(TEST_FIRST_NAME, TEST_LAST_NAME, USED_EMAIL, TEST_PASSWORD));

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private UserRepository userRepository;

    @Test
    public void signupSuccess() {
        when(userRepository.findByEmail(USED_EMAIL)).thenReturn(EXISTING_ENTITY);

        assertThat(getActualResponse(TEST_EMAIL, TEST_PASSWORD)).contains(SUCCESS);
        verify(userRepository, times(0)).findByEmail(USED_EMAIL);
        verify(userRepository).findByEmail(TEST_EMAIL);
    }

    @Test
    public void signupBadEmail() {
        when(userRepository.findByEmail(USED_EMAIL)).thenReturn(EXISTING_ENTITY);

        assertThat(getActualResponse(USED_EMAIL, TEST_PASSWORD)).contains(EMAIL_IN_USE);
        assertThat(getActualResponse("@nure.ua", TEST_PASSWORD))
                .contains(INCORRECT_EMAIL_FORMAT);
        assertThat(getActualResponse("dmytro.zhmur@nure.u", TEST_PASSWORD))
                .contains(INCORRECT_EMAIL_FORMAT);
        verify(userRepository).findByEmail(USED_EMAIL);
    }

    @Test
    public void signupBadPassword() {
        when(userRepository.findByEmail(USED_EMAIL)).thenReturn(EXISTING_ENTITY);

        assertThat(getActualResponse(TEST_EMAIL, "passwordpasswordpassword"))
                .contains(INVALID_PASSWORD_LENGTH);
        assertThat(getActualResponse(TEST_EMAIL, "passwor"))
                .contains(INVALID_PASSWORD_LENGTH);
        verify(userRepository, times(0)).findByEmail(USED_EMAIL);
        verify(userRepository, times(2)).findByEmail(TEST_EMAIL);
    }

    private String getActualResponse(String email, String password) {
        return this.restTemplate.postForObject(
                        String.format("http://localhost:%d/api/v1/signup", port),
                        new SignupRequest(TEST_FIRST_NAME, TEST_LAST_NAME, email, password),
                        String.class);
    }
}
