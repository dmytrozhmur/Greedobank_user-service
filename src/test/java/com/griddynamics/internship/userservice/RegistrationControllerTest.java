package com.griddynamics.internship.userservice;

import com.griddynamics.internship.userservice.communication.request.SignupRequest;
import com.griddynamics.internship.userservice.communication.response.JsonResponse;
import com.griddynamics.internship.userservice.controller.auth.RegistrationController;
import com.griddynamics.internship.userservice.datasource.repo.RoleRepository;
import com.griddynamics.internship.userservice.datasource.repo.UserRepository;
import com.griddynamics.internship.userservice.model.Role;
import com.griddynamics.internship.userservice.model.RoleTitle;
import com.griddynamics.internship.userservice.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static com.griddynamics.internship.userservice.utils.AuthenticationUtils.URL_FORMAT;
import static com.griddynamics.internship.userservice.utils.AuthenticationUtils.signinUser;
import static com.griddynamics.internship.userservice.utils.ResponseMessages.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@WithUserDetails(RegistrationControllerTest.USED_EMAIL)
@Sql(value = "insert_first_user.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class RegistrationControllerTest {
    public static final String USED_EMAIL = "dmytro.zhmur@nure.ua";
    private static final String USED_PASSWORD = "password1";
    private static final String TEST_PASSWORD = "password";
    private static final String TARGET = "signup";
    public static final int DEFAULT_USER_COUNT = 10;
    public static final String USED_FIRSTNAME = "Dmytro";
    public static final String USED_LASTNAME = "Zhmur";
    public static final String TEST_EMAIL = "ykomiahina@griddynamics.com";
    public static final String TEST_FIRSTNAME = "Yevheniia";
    public static final String TEST_LASTNAME = "Komiahina";

    private static User existingUser;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private RegistrationController registrationController;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @BeforeEach
    private void fillDataBase() {
        Role adminRole = new Role(1, RoleTitle.ROLE_ADMIN);
       // roleRepository.save(adminRole);

        existingUser = new User(
                USED_FIRSTNAME,
                USED_LASTNAME,
                USED_EMAIL,
                USED_PASSWORD,
                adminRole
        );
       // userRepository.save(existingUser);
    }

    @Test
    public void checkDataBase() {
//        List<User> all = userRepository.findAll();
//        User actual = all.get(0);
        User actual = userRepository.findByEmail(USED_EMAIL);

        assertEquals(existingUser, actual);
    }

    @Test
    public void signupSuccess() {
//        String token =
//                signinUser(this.restTemplate, USED_EMAIL, USED_PASSWORD, this.port)
//                        .get("token")
//                        .toString();
//        String targetUrl = String.format(URL_FORMAT, port, TARGET);
//
//        String actual = getActual(
//                token,
//                targetUrl,
//                new SignupRequest(
//                        "Pavlo",
//                        "Zibrov",
//                        String.format(
//                                "pzibrov%d@griddynamics.com",
//                                userRepository.findAll().size() - DEFAULT_USER_COUNT),
//                        TEST_PASSWORD
//                )
//        );

        ResponseEntity<JsonResponse<String>> actualResponse =
                registrationController.registerUser(new SignupRequest(
                        TEST_FIRSTNAME,
                        TEST_LASTNAME,
                        TEST_EMAIL,
                        "password2"
                ));
        String responseMessage = actualResponse.getBody().getContent();
        User registered = userRepository.findByEmail(TEST_EMAIL);

        assertThat(responseMessage).contains(SUCCESS);
        assertEquals(TEST_FIRSTNAME, registered.getFirstName());
        assertEquals(TEST_LASTNAME, registered.getLastName());
    }

    @Test
    public void signupInvalidField() {
        String token =
                signinUser(this.restTemplate, USED_LASTNAME, USED_PASSWORD, this.port)
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
                signinUser(this.restTemplate, USED_LASTNAME, USED_PASSWORD, this.port)
                        .get("token")
                        .toString();
        String targetUrl = String.format(URL_FORMAT, port, TARGET);

        String actual = getActual(token, targetUrl,
                new SignupRequest(
                        USED_FIRSTNAME,
                        USED_LASTNAME,
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
