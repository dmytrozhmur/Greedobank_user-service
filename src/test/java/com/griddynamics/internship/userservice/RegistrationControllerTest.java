package com.griddynamics.internship.userservice;

import com.griddynamics.internship.userservice.communication.request.SignupRequest;
import com.griddynamics.internship.userservice.datasource.repo.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import static com.griddynamics.internship.userservice.utils.AuthenticationUtils.URL_FORMAT;
import static com.griddynamics.internship.userservice.utils.AuthenticationUtils.signinUser;
import static com.griddynamics.internship.userservice.utils.ResponseMessages.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RegistrationControllerTest {
    private static final String USED_EMAIL = "dzhmur@griddynamics.com";
    private static final String USED_PASSWORD = "password1";
    private static final String TEST_PASSWORD = "password";
    private static final String TARGET = "signup";
    public static final int DEFAULT_USER_COUNT = 10;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void signupSuccess() {
        String token =
                signinUser(this.restTemplate, USED_EMAIL, USED_PASSWORD, this.port)
                        .get("token")
                        .toString();
        String targetUrl = String.format(URL_FORMAT, port, TARGET);

        String actual = getActual(
                token,
                targetUrl,
                new SignupRequest(
                        "Pavlo",
                        "Zibrov",
                        String.format(
                                "pzibrov%d@griddynamics.com",
                                userRepository.findAll().size() - DEFAULT_USER_COUNT),
                        TEST_PASSWORD
                )
        );

        assertThat(actual).contains(SUCCESS);
    }

    @Test
    public void signupInvalidField() {
        String token =
                signinUser(this.restTemplate, USED_EMAIL, USED_PASSWORD, this.port)
                        .get("token")
                        .toString();
        String targetUrl = String.format(URL_FORMAT, port, TARGET);

        String actual = getActual(
                token,
                targetUrl,
                new SignupRequest(
                        "",
                        "Zibrov",
                        "svjndsglvnlngjbkmgfblkghbfkdjnfdvjlmrlmtbkrtb@gmail.u",
                        "pass"
        ));

        assertThat(actual).contains(EMPTY_FIELD, EXCEEDED_SIZE, INCORRECT_FORMAT, INVALID_PASSWORD_LENGTH);
    }

    @Test
    public void signupExistingEmail() {
        String token =
                signinUser(this.restTemplate, USED_EMAIL, USED_PASSWORD, this.port)
                        .get("token")
                        .toString();
        String targetUrl = String.format(URL_FORMAT, port, TARGET);

        String actual = getActual(token, targetUrl,
                new SignupRequest(
                        "Dmytro",
                        "Zhmur",
                        USED_EMAIL,
                        TEST_PASSWORD
                )
        );

        assertThat(actual).contains(EMAIL_IN_USE);
    }

    private String getActual(String token, String targetUrl, SignupRequest signup) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<Object> request = new HttpEntity<>(
                signup,
                headers
        );

        ResponseEntity<String> response = this.restTemplate.exchange(
                targetUrl, HttpMethod.POST, request, String.class
        );
        String actual = response.getBody();
        return actual;
    }
}
