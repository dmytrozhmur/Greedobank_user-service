package com.griddynamics.internship.userservice;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class UserControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void getUserListReturnsDefaultMessage() {
        assertThat(
                this.restTemplate
                        .getForObject(String
                                .format("http://localhost:%d/api/v1/users", port), String.class))
                .contains("dzhmur@griddynamics.com", "ykomiahina@griddynamics.com",
                        "okukurik@girddynamics.com", "isubota@griddynamics.com", "tkomarova@griddynamics.com");
    }
}
