package com.griddynamics.internship.userservice;

import com.griddynamics.internship.userservice.communication.request.SigninRequest;
import com.griddynamics.internship.userservice.communication.response.JsonResponse;
import com.griddynamics.internship.userservice.model.JwtUser;
import com.griddynamics.internship.userservice.utils.JwtUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public class AuthenticationTest {
    private static final String TEST_EMAIL = "dzhmur@griddynamics.com";
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void authenticate() {
        JsonResponse<LinkedHashMap<String, Object>> actual = this.restTemplate.postForObject(
                String.format("http://localhost:%d/api/v1/signin", port),
                new SigninRequest(TEST_EMAIL, "password1"),
                JsonResponse.class);
        Map<String, Object> user = actual.getContent();

        assertThat(user.get("id")).isEqualTo(1);
        assertThat(user.get("username")).isEqualTo("dmytrozhmur1");
        assertThat(user.get("type")).isEqualTo("Bearer");
        assertThat(user.get("email")).isEqualTo(TEST_EMAIL);
        assertThat(JwtUtils.getEmailBy(user.get("token").toString())).isEqualTo(TEST_EMAIL);
        assertThat(user.get("roles")).isEqualTo(new ArrayList<>());
    }
}
