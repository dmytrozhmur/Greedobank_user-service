package com.griddynamics.internship.userservice.integration;

import com.griddynamics.internship.userservice.communication.request.SigninRequest;
import com.griddynamics.internship.userservice.communication.response.JsonResponse;
import com.griddynamics.internship.userservice.model.user.JwtUser;
import com.griddynamics.internship.userservice.component.processor.JwtProcessor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.ContextConfiguration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ContextConfiguration
public class AuthenticationTest extends IntegrationTest {
    private static final String TEST_EMAIL = "dmytro.zhmur@nure.ua";
    private static final String TEST_PASSWORD = "password";
    private static final String SPECIAL_TARGET = "signin";
    @Autowired
    private JwtProcessor jwtProcessor;

    @Test
    public void authenticationSuccess() {
        JwtUser authUser = signinUser(restTemplate, port);

        assertThat(authUser.getId()).isEqualTo(1);
        assertThat(authUser.getType()).isEqualTo("Bearer");
        assertThat(authUser.getEmail()).isEqualTo(TEST_EMAIL);
        assertThat(jwtProcessor.getEmail(authUser.getAccessToken())).isEqualTo(TEST_EMAIL);
    }

    public static JwtUser signinUser(TestRestTemplate restTemplate, int port, String... credentials) {
        String specialUrl = String.format(URL_FORMAT, port, SPECIAL_TARGET);
        if(credentials.length != 2) {
            credentials = new String[] {TEST_EMAIL, TEST_PASSWORD};
        }

        HttpEntity<SigninRequest> httpEntity = new HttpEntity<>(new SigninRequest(
                credentials[0],
                credentials[1]
        ));
        JsonResponse<JwtUser> actualResponse = restTemplate.exchange(
                specialUrl,
                HttpMethod.POST,
                httpEntity,
                new ParameterizedTypeReference<JsonResponse<JwtUser>>() {}
        ).getBody();

        JwtUser user = actualResponse.getContent();
        return user;
    }
}
