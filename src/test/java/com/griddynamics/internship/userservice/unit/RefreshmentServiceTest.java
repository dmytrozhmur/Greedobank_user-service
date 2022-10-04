package com.griddynamics.internship.userservice.unit;

import com.griddynamics.internship.userservice.model.role.Role;
import com.griddynamics.internship.userservice.model.token.JwtRefreshment;
import com.griddynamics.internship.userservice.model.token.Refreshment;
import com.griddynamics.internship.userservice.model.user.User;
import com.griddynamics.internship.userservice.repo.RefreshmentRepository;
import com.griddynamics.internship.userservice.repo.RoleRepository;
import com.griddynamics.internship.userservice.repo.UserRepository;
import com.griddynamics.internship.userservice.service.RefreshmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.Optional;

import static com.griddynamics.internship.userservice.model.role.RoleTitle.defaultTitle;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class RefreshmentServiceTest {
    private static final String EMAIL_PARAMETER = "dmytro.zhmur@nure.ua";
    private static final String TEST_FIRSTNAME = "Dmytro";
    private static final String TEST_LASTNAME = "Zhmur";
    private static final String TEST_PASSWORD = "password";
    private static final String TEST_REFRESH_TOKEN = "abcdef-ghijkl-mnopqr-stuvw-xyz";
    public static final String TOKEN_TYPE = "Bearer";
    private static final int TEST_ID = 1;
    @MockBean
    private RefreshmentRepository refreshmentRepository;
    @MockBean
    private RoleRepository roleRepository;
    @MockBean
    private UserRepository userRepository;
    @Autowired
    private RefreshmentService service;

    @BeforeEach
    private void mockRepo() {
        MockitoAnnotations.openMocks(this);
        when(userRepository
                .findAuthorizationInfoByEmail(Pageable.unpaged(), EMAIL_PARAMETER))
                .thenReturn(new PageImpl<>(Collections.singletonList(createTestUser())));
        when(refreshmentRepository.findByToken(TEST_REFRESH_TOKEN)).thenReturn(createTestToken());
    }

    @Test
    public void generateRefreshToken() {
        Refreshment refresh = service.generateRefreshToken(EMAIL_PARAMETER);
        User refreshOwner = refresh.getUser();

        assertNotNull(refresh.getToken());
        assertEquals(refreshOwner.getEmail(), EMAIL_PARAMETER);
        assertEquals(refreshOwner.getFirstName(), TEST_FIRSTNAME);
        assertEquals(refreshOwner.getLastName(), TEST_LASTNAME);
        assertEquals(refreshOwner.getPassword(), TEST_PASSWORD);
    }

    @Test
    public void updateAccessToken() {
        JwtRefreshment refreshmentDto = service.updateAccessToken(TEST_REFRESH_TOKEN);

        assertNotNull(refreshmentDto.getAccessToken());
        assertEquals(TEST_REFRESH_TOKEN, refreshmentDto.getRefreshToken());
        assertEquals(TOKEN_TYPE, refreshmentDto.getTokenType());
    }

    private Optional<Refreshment> createTestToken() {
        return Optional.of(new Refreshment(
                TEST_REFRESH_TOKEN,
                createTestUser()
        ));
    }

    private static Role createTestRole() {
        return new Role(1, defaultTitle());
    }

    private static User createTestUser() {
        return new User(
                TEST_ID,
                TEST_FIRSTNAME,
                TEST_LASTNAME,
                EMAIL_PARAMETER,
                TEST_PASSWORD,
                createTestRole()
        );
    }
}
