package com.griddynamics.internship.userservice;

import com.griddynamics.internship.userservice.repo.RoleRepository;
import com.griddynamics.internship.userservice.utils.JwtUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.Map;

import static com.griddynamics.internship.userservice.utils.AuthenticationUtils.signinUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public class AuthenticationTest {
    private static final String TEST_EMAIL = "dzhmur@griddynamics.com";
    private static final String TEST_PASSWORD = "password1";
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void authenticate() {
        Map<String, Object> user = signinUser(this.restTemplate, TEST_EMAIL, TEST_PASSWORD, this.port);

        assertThat(user.get("id")).isEqualTo(1);
        assertThat(user.get("type")).isEqualTo("Bearer");
        assertThat(user.get("email")).isEqualTo(TEST_EMAIL);
        assertThat(JwtUtils.getEmail(user.get("token").toString())).isEqualTo(TEST_EMAIL);
    }
}
