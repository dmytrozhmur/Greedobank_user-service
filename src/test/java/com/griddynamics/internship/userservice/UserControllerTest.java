package com.griddynamics.internship.userservice;

import com.fasterxml.jackson.core.JsonParser;
import com.griddynamics.internship.userservice.communication.request.SigninRequest;
import com.griddynamics.internship.userservice.model.UserDTO;
import org.apache.tomcat.util.json.JSONParser;
import org.apache.tomcat.util.json.Token;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import java.net.URI;
import java.net.URISyntaxException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class UserControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void getUserListReturnsDefaultMessage() {
        String url = String.format("http://localhost:%d/api/v1/users", port);

        assertThat(
                this.restTemplate
                        .getForObject(url, String.class))
                .contains("dzhmur@griddynamics.com", "ykomiahina@griddynamics.com",
                        "okukurik@girddynamics.com", "isubota@griddynamics.com", "tkomarova@griddynamics.com");
    }
}
