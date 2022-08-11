package com.griddynamics.internship.userservice;

import com.griddynamics.internship.userservice.model.Role;
import com.griddynamics.internship.userservice.model.RoleTitle;
import com.griddynamics.internship.userservice.model.UserDTO;
import com.griddynamics.internship.userservice.service.UserService;
import com.griddynamics.internship.userservice.utils.IntegerParameterResolver;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;
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
@ExtendWith(IntegerParameterResolver.class)
public class UserControllerTest {
    private static final String UNKNOWN_SENDER = "\"Full authentication is required to access this resource\"";
    private static final String ACCESS_DENIED = "\"Access is denied\"";
    public static final String TARGET = "users/";
    public static final String USER_EMAIL = "tchornyi@griddynamics.com";
    public static final String USER_PASSWORD = "password6";
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private UserService userService;

    @Test
    public void getUserListReturnsDefaultMessage() {
        HttpEntity<Object> request = createAuthorizedRequest ("dzhmur@griddynamics.com", "password1");
        String targetUrl = String.format(URL_FORMAT, port, TARGET);

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
    public void getUserById(Integer id) {
        HttpEntity<Object> request = createAuthorizedRequest(USER_EMAIL, USER_PASSWORD);
        String targetUrl = String.format(URL_FORMAT, port, TARGET + id);

        ResponseEntity<UserDTO> response = this.restTemplate.exchange(
                targetUrl, HttpMethod.GET, request, UserDTO.class
        );
        UserDTO actual = response.getBody();

        assertThat(actual.getId()).isEqualTo(id);
        assertThat(actual.getFirstName()).isEqualTo("Taras");
        assertThat(actual.getLastName()).isEqualTo("Chornyi");
        assertThat(actual.getEmail()).isEqualTo(USER_EMAIL);
        assertThat(actual.getRole().getTitle()).isEqualTo(RoleTitle.valueOf("ROLE_USER"));
    }

    @Test
    public void getUserListUnauthorized() {
        String targetUrl = String.format(URL_FORMAT, port, TARGET);

        String actual = this.restTemplate.getForObject(targetUrl, String.class);

        assertThat(actual).isEqualTo(UNKNOWN_SENDER);
    }

    @Test
    public void getUserListInaccessible() {
        HttpEntity<Object> request = createAuthorizedRequest(USER_EMAIL, USER_PASSWORD);
        String targetUrl = String.format(URL_FORMAT, port, TARGET);

        ResponseEntity<String> response = this.restTemplate.exchange(
                targetUrl, HttpMethod.GET, request, String.class
        );
        String actual = response.getBody();

        assertThat(actual).isEqualTo(ACCESS_DENIED);
    }

    @Test
    public void getOtherThanRequesterUser(Integer id) {
        HttpEntity<Object> request = createAuthorizedRequest("ipadalka@griddynamics.com", "password7");
        String targetUrl = String.format(URL_FORMAT, port, TARGET + id);

        ResponseEntity<String> response = this.restTemplate.exchange(
                targetUrl, HttpMethod.GET, request, String.class
        );
        String actual = response.getBody();

        assertThat(actual).isEqualTo(ACCESS_DENIED);
    }

    private HttpEntity<Object> createAuthorizedRequest(String email, String password) {
        String token = signinUser(this.restTemplate, email, password, this.port)
                .get("token")
                .toString();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        return new HttpEntity<>(headers);
    }
}
