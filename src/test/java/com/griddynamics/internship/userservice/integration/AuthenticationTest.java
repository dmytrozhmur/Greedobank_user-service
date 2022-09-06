package com.griddynamics.internship.userservice.integration;

import com.griddynamics.internship.userservice.communication.request.SigninRequest;
import com.griddynamics.internship.userservice.communication.response.JsonResponse;
import com.griddynamics.internship.userservice.model.user.JwtUser;
import com.griddynamics.internship.userservice.repo.UserRepository;
import com.griddynamics.internship.userservice.utils.JwtUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ContextConfiguration
@Sql(value = "insert_first_users.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = "clear_database.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class AuthenticationTest {
    private static final String TEST_EMAIL = "dmytro.zhmur@nure.ua";
    private static final String TEST_PASSWORD = "password";
    private static final String URL_FORMAT = "http://localhost:%d/api/v1/signin";
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private UserRepository userRepository;

    @Test
    public void authenticationSuccess() {
        JwtUser authUser = signinUser(restTemplate, port);

        assertThat(authUser.getId()).isEqualTo(1);
        assertThat(authUser.getType()).isEqualTo("Bearer");
        assertThat(authUser.getEmail()).isEqualTo(TEST_EMAIL);
        assertThat(JwtUtils.getEmail(authUser.getAccessToken())).isEqualTo(TEST_EMAIL);
    }

    public static JwtUser signinUser(TestRestTemplate restTemplate, int port, String... credentials) {
        if(credentials.length != 2) {
            credentials = new String[] {TEST_EMAIL, TEST_PASSWORD};
        }

        HttpEntity<SigninRequest> httpEntity = new HttpEntity<>(new SigninRequest(
                credentials[0],
                credentials[1]
        ));
        JsonResponse<JwtUser> actualResponse = restTemplate.exchange(
                String.format(URL_FORMAT, port),
                HttpMethod.POST,
                httpEntity,
                new ParameterizedTypeReference<JsonResponse<JwtUser>>() {}
        ).getBody();

        JwtUser user = actualResponse.getContent();
        return user;
    }
}
