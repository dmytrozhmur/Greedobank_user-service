package integration;

import com.griddynamics.internship.userservice.communication.request.UserDataRequest;
import com.griddynamics.internship.userservice.communication.response.JsonResponse;
import com.griddynamics.internship.userservice.repo.UserRepository;
import com.griddynamics.internship.userservice.model.user.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;

import java.util.Arrays;
import java.util.Map;

import static com.griddynamics.internship.userservice.utils.ResponseMessages.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RegistrationControllerTest extends IntegrationTest {
    public static final String FIRST_USED_EMAIL = "dmytro.zhmur@nure.ua";
    private static final String TEST_PASSWORD = "password";
    public static final String USED_FIRSTNAME = "Dmytro";
    public static final String USED_LASTNAME = "Zhmur";
    public static final String TEST_EMAIL = "ykomiahina@griddynamics.com";
    public static final String TEST_FIRSTNAME = "Yevheniia";
    public static final String TEST_LASTNAME = "Komiahina";
    public static final int EXPECTED_ID = 11;
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void signupSuccess() {
        HttpEntity<UserDataRequest> httpEntity = new HttpEntity<>(
                new UserDataRequest(
                        TEST_FIRSTNAME,
                        TEST_LASTNAME,
                        TEST_EMAIL,
                        "password2"
                ),
                httpHeaders
        );
        JsonResponse actualResponse = restTemplate.exchange(
                targetUrl,
                HttpMethod.POST,
                httpEntity,
                JsonResponse.class
        ).getBody();

        String actualMessage = actualResponse.getContent().toString();
        User actualRegistered = userRepository
                .findById(EXPECTED_ID).get();

        assertEquals(SUCCESS, actualMessage);
        assertEquals(TEST_FIRSTNAME, actualRegistered.getFirstName());
        assertEquals(TEST_LASTNAME, actualRegistered.getLastName());
    }

    @Test
    public void signupInvalidField() {
        HttpEntity<UserDataRequest> httpEntity = new HttpEntity<>(
                new UserDataRequest(
                        "",
                        "Zibrov",
                        "svjndsglvnlngjbkmgfblkghbfkdjnfdvjlmrlmtbkrtb@gmail.u",
                        "pass"
                ),
                httpHeaders
        );
        JsonResponse actualResponse = restTemplate.exchange(
                targetUrl,
                HttpMethod.POST,
                httpEntity,
                JsonResponse.class
        ).getBody();
        String actualMessage = actualResponse.getContent().toString();
        Map<String, String[]> actualErrors = actualResponse.getErrors();

        assertEquals(FAILURE, actualMessage);
        assertEquals(INAPPROPRIATE_SIZE, actualErrors.get("firstName")[0]);
        assertEquals(INVALID_PASSWORD_LENGTH, actualErrors.get("password")[0]);
        assertThat(Arrays.stream(actualErrors.get("email")).toList()).contains(INAPPROPRIATE_SIZE, INCORRECT_FORMAT);
    }

    @Test
    public void signupExistingEmail() {
        HttpEntity<UserDataRequest> httpEntity = new HttpEntity<>(
                new UserDataRequest(
                        USED_FIRSTNAME,
                        USED_LASTNAME,
                        FIRST_USED_EMAIL,
                        TEST_PASSWORD
                ),
                httpHeaders
        );
        JsonResponse actualResponse = restTemplate.exchange(
                targetUrl,
                HttpMethod.POST,
                httpEntity,
                JsonResponse.class
        ).getBody();
        String actualMessage = actualResponse.getContent().toString();
        Map<String, String[]> actualErrors = actualResponse.getErrors();

        assertEquals(FAILURE, actualMessage);
        assertEquals(EMAIL_IN_USE, actualErrors.get("email")[0]);
    }

    @Override
    public String getTarget() {
        return "signup";
    }
}
