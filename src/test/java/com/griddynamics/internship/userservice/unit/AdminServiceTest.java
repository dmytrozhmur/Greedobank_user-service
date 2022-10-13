package com.griddynamics.internship.userservice.unit;

import com.griddynamics.internship.userservice.communication.response.CardTemplatePage;
import com.griddynamics.internship.userservice.component.processor.RequestEntityProcessor;
import com.griddynamics.internship.userservice.model.card.CardTemplateDTO;
import com.griddynamics.internship.userservice.model.card.TariffDTO;
import com.griddynamics.internship.userservice.model.role.Role;
import com.griddynamics.internship.userservice.model.role.RoleTitle;
import com.griddynamics.internship.userservice.model.user.UserDTO;
import com.griddynamics.internship.userservice.service.AdminService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.griddynamics.internship.userservice.unit.RefreshmentServiceTest.TEST_REFRESH_TOKEN;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AdminServiceTest extends UserServiceTest {
    private static final int FIRST_TEST_ID = 1;
    private static final int SECOND_TEST_ID = 2;
    @Autowired
    private AdminService adminService;
    @MockBean
    private RestTemplate restTemplate;
    @MockBean
    private RequestEntityProcessor entityProcessor;
    @Value("${card-service.url.card-templates}")
    private String templatesUrl;

    @Test
    public void getAllAdmins() {
        Collection<UserDTO> expected = getAllExpectedUsers();
        Role adminRole = roleRepository.findByTitle(RoleTitle.ROLE_ADMIN);
        when(userRepository
                .findAllByRole(pageRequest, adminRole))
                .thenReturn(new PageImpl<>(mockedUsers));

        Collection<UserDTO> actual = adminService.findAll(TEST_PAGE, TEST_PAGE_SIZE).getContent();

        verify(userRepository).findAllByRole(pageRequest, adminRole);
        assertThat(actual, is(expected));
    }

    @Test
    public void getAdminById() {
        UserDTO expected = getAllExpectedUsers().get(0);

        when(userRepository.findByIdAndRole(TEST_ID, TEST_ADMIN_ROLE))
                .thenReturn(Optional.ofNullable(mockedUsers.get(0)));

        UserDTO actual = adminService.findAdmin(TEST_ID);

        assertThat(actual, is(expected));
    }

    @Test
    public void getCardTemplatesByAdminId() {
        List<CardTemplateDTO> allTemplates = getAllExpectedTemplates();
        HttpHeaders testHeaders = new HttpHeaders();
        testHeaders.setBearerAuth(TEST_REFRESH_TOKEN);
        HttpEntity<String> testEntity = new HttpEntity<>(testHeaders);

        when(userRepository.existsByIdAndRole(FIRST_TEST_ID, TEST_ADMIN_ROLE)).thenReturn(true);
        when(userRepository.existsByIdAndRole(SECOND_TEST_ID, TEST_ADMIN_ROLE)).thenReturn(true);
        when(entityProcessor.generateEntityForCurrUserAuthorization())
                .thenReturn(testEntity);
        when(restTemplate.exchange(templatesUrl, HttpMethod.GET, testEntity, CardTemplatePage.class))
                .thenReturn(ResponseEntity.ok(new CardTemplatePage(allTemplates)));

        Page<CardTemplateDTO> actualFirstAdminTemplates = adminService.findCardTemplates(FIRST_TEST_ID);
        Page<CardTemplateDTO> actualSecondAdminTemplates = adminService.findCardTemplates(SECOND_TEST_ID);

        verify(restTemplate, times(2)).exchange(templatesUrl, HttpMethod.GET, testEntity, CardTemplatePage.class);
        assertThat(actualFirstAdminTemplates, is(new CardTemplatePage(Collections.singletonList(allTemplates.get(0)))));
        assertThat(actualSecondAdminTemplates, is(new CardTemplatePage(Collections.singletonList(allTemplates.get(1)))));
    }

    private List<CardTemplateDTO> getAllExpectedTemplates() {
        return Arrays.asList(
                new CardTemplateDTO(1, "GOLD", new TariffDTO(15d, 16d, 17d, "UAH"), OffsetDateTime.MIN, OffsetDateTime.MAX, 1),
                new CardTemplateDTO(2, "PREMIUM", new TariffDTO(15d, 16d, 17d, "UAH"), OffsetDateTime.MIN, OffsetDateTime.MAX, 2)
        );
    }
}
