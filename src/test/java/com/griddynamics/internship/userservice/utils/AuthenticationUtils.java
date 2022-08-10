package com.griddynamics.internship.userservice.utils;

import com.griddynamics.internship.userservice.communication.request.SigninRequest;
import com.griddynamics.internship.userservice.communication.response.JsonResponse;
import org.springframework.boot.test.web.client.TestRestTemplate;

import java.util.LinkedHashMap;
import java.util.Map;

public class AuthenticationUtils {
    public static final String URL_FORMAT = "http://localhost:%d/api/v1/%s";
    private static final String TARGET = "signin";

    public static Map<String, Object> signinUser(TestRestTemplate restTemplate,
                                                 String email,
                                                 String password,
                                                 int port) {
        JsonResponse<LinkedHashMap<String, Object>> actual = (JsonResponse<LinkedHashMap<String, Object>>) restTemplate.postForObject(
                String.format(URL_FORMAT, port, TARGET),
                new SigninRequest(email, password),
                JsonResponse.class);
        Map<String, Object> user = actual.getContent();
        return user;
    }
}
