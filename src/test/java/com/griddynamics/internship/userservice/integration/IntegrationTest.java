package com.griddynamics.internship.userservice.integration;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.jdbc.Sql;

import static com.griddynamics.internship.userservice.integration.AuthenticationTest.signinUser;
import static org.h2.engine.Constants.URL_FORMAT;

@Sql(value = "insert_first_users.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = "clear_database.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public abstract class IntegrationTest {
    protected static final String URL_FORMAT = "http://localhost:%d/api/v1/%s";
    protected static String targetUrl;
    protected HttpHeaders httpHeaders = new HttpHeaders();
    protected String target;
    @LocalServerPort
    protected int port;
    @Autowired
    protected TestRestTemplate restTemplate;

    @BeforeEach
    public void buildPath() {
        targetUrl = String.format(URL_FORMAT, port, getTarget());
        httpHeaders.setBearerAuth(signinUser(restTemplate, port).getAccessToken());
    }

    public String getTarget() {
        return target;
    }
}
