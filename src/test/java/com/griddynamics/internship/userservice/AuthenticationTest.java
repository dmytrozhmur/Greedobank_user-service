package com.griddynamics.internship.userservice;

import com.griddynamics.internship.userservice.communication.request.SigninRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public class AuthenticationTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void authenticate() {
        String actual = this.restTemplate.postForObject(
                String.format("http://localhost:%d/api/v1/signin", port),
                new SigninRequest("dzhmur@griddynamics.com", "password1"),
                String.class);

        assertThat(actual).contains("token", "Bearer", "dmytrozhmur");
    }
}
