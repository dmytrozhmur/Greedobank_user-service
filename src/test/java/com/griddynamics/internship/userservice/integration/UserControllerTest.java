package com.griddynamics.internship.userservice.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.griddynamics.internship.userservice.communication.response.JsonResponse;
import com.griddynamics.internship.userservice.model.user.UserDTO;
import com.griddynamics.internship.userservice.repo.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.griddynamics.internship.userservice.integration.AuthenticationTest.signinUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Sql(value = "insert_first_users.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = "clear_database.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class UserControllerTest {
    private static final String UNKNOWN_SENDER = "Full authentication is required to access this resource";
    private static final String ACCESS_DENIED = "Access is denied";
    private static final int USER_ID = 2;
    private static final String USER_EMAIL = "okukurik@girddynamics.com";
    private static final String USER_PASSWORD = "password3";
    private static final String USER_FIRSTNAME = "Oleksandr";
    private static final String USER_LASTNAME = "Kukurik";
    private static final String URL_FORMAT = "http://localhost:%d/api/v1/users/";
    private String targetUrl;
    private HttpHeaders httpHeaders = new HttpHeaders();

    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void buildPath() {
        targetUrl = String.format(URL_FORMAT, port);
        httpHeaders.setBearerAuth(signinUser(restTemplate, port).getAccessToken());

        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(Collections.singletonList(MediaType.APPLICATION_OCTET_STREAM));
        RestTemplate mainTemplate = restTemplate.getRestTemplate();
        List<HttpMessageConverter<?>> converters = mainTemplate.getMessageConverters();
        converters.add(converter);
        mainTemplate.setMessageConverters(converters);
    }

    @Test
    public void getUserListSuccess() {
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
        ArrayList<UserDTO> actualResponse = restTemplate.exchange(
                targetUrl,
                HttpMethod.GET,
                httpEntity,
                new ParameterizedTypeReference<ArrayList<UserDTO>>() {}
        ).getBody();

        assertEquals(userRepository.findAll().stream().map(UserDTO::new).toList(), actualResponse);
    }

    @Test
    public void getUserListUnauthorized() {
        JsonResponse<String> actualResponse = restTemplate.exchange(
                targetUrl,
                HttpMethod.GET,
                new HttpEntity<>(null),
                new ParameterizedTypeReference<JsonResponse<String>>() {}
        ).getBody();

        assertEquals(UNKNOWN_SENDER, actualResponse.getContent());
    }
    @Test
    public void getUserListInaccessible() {
        httpHeaders.setBearerAuth(signinUser(restTemplate, port, USER_EMAIL, USER_PASSWORD).getAccessToken());

        JsonResponse<String> actualResponse = restTemplate.exchange(
                targetUrl,
                HttpMethod.GET,
                new HttpEntity<>(httpHeaders),
                new ParameterizedTypeReference<JsonResponse<String>>() {}
        ).getBody();

        assertEquals(ACCESS_DENIED, actualResponse.getContent());
    }

    @Test
    public void getUserByIdSuccess() {
        targetUrl += USER_ID;

        UserDTO actualResponse = restTemplate.exchange(
                targetUrl,
                HttpMethod.GET,
                new HttpEntity<>(httpHeaders),
                new ParameterizedTypeReference<JsonResponse<UserDTO>>() {}
        ).getBody().getContent();

        assertEquals(USER_ID, actualResponse.getId());
        assertEquals(USER_EMAIL, actualResponse.getEmail());
        assertEquals(USER_FIRSTNAME, actualResponse.getFirstName());
        assertEquals(USER_LASTNAME, actualResponse.getLastName());
    }
}