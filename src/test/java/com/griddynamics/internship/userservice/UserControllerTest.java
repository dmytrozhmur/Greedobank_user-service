package com.griddynamics.internship.userservice;

import com.griddynamics.internship.userservice.communication.request.SigninRequest;
import com.griddynamics.internship.userservice.communication.response.JsonResponse;
import com.griddynamics.internship.userservice.model.Role;
import com.griddynamics.internship.userservice.model.RoleTitle;
import com.griddynamics.internship.userservice.model.UserDTO;
import com.griddynamics.internship.userservice.service.UserService;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.griddynamics.internship.userservice.utils.AuthenticationUtils.URL_FORMAT;
import static com.griddynamics.internship.userservice.utils.AuthenticationUtils.signinUser;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class UserControllerTest {
    private static final String UNKNOWN_SENDER_EXCEPTION_MESSAGE = "\"Full authentication is required to access this resource\"";
    private static final String ACCESS_DENIED_EXCEPTION_MESSAGE = "\"Access is denied\"";
    public static final String TARGET = "users";
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private UserService userService;

    @Test
    public void getUserListReturnsDefaultMessage() {
        String token = signinUser(this.restTemplate,
                        "dzhmur@griddynamics.com",
                        "password1",
                        this.port
        ).get("token").toString();
        String targetUrl = String.format(URL_FORMAT, port, "users");

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<Object> request = new HttpEntity<>(headers);

        ResponseEntity<ArrayList> response = this.restTemplate.exchange(
                targetUrl, HttpMethod.GET, request, ArrayList.class
        );
        List<UserDTO> actual = (List<UserDTO>) response.getBody().stream()
                .map(el -> {
                    Map<String, Object> userData = (LinkedHashMap<String, Object>) el;
                    Map<String, Object> roleData = (LinkedHashMap<String, Object>) userData.get("role");
                    return new UserDTO(
                            (Integer) userData.get("id"),
                            (String) userData.get("firstName"),
                            (String) userData.get("lastName"),
                            (String) userData.get("email"),
                            new Role(
                                    (Integer) roleData.get("id"),
                                    RoleTitle.valueOf((String) roleData.get("title"))
                            )
                    );
                }).collect(Collectors.toList());

        assertThat(actual).contains(userService.findAll().toArray(UserDTO[]::new));
    }

    @Test
    public void getUserListUnauthorized() {
        String targetUrl = String.format(URL_FORMAT, port, TARGET);

        String actual = this.restTemplate.getForObject(
                targetUrl,
                String.class
        );

        assertThat(actual).isEqualTo(UNKNOWN_SENDER_EXCEPTION_MESSAGE);
    }

    @Test
    public void getUserListInaccessible() {
        String token = signinUser(
                this.restTemplate,
                "tchornyi@griddynamics.com",
                "password6",
                this.port
        ).get("token").toString();
        String targetUrl = String.format(URL_FORMAT, port, TARGET);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<Object> request = new HttpEntity<>(headers);

        ResponseEntity<String> response = this.restTemplate.exchange(
                targetUrl, HttpMethod.GET, request, String.class
        );
        String actual = response.getBody();

        assertThat(actual).isEqualTo(ACCESS_DENIED_EXCEPTION_MESSAGE);
    }
}
