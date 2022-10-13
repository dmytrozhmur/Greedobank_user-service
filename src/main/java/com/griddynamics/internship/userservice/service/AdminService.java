package com.griddynamics.internship.userservice.service;

import com.griddynamics.internship.userservice.communication.mapper.ResponseMapper;
import com.griddynamics.internship.userservice.communication.response.CardTemplatePage;
import com.griddynamics.internship.userservice.communication.response.UserPage;
import com.griddynamics.internship.userservice.component.processor.JwtProcessor;
import com.griddynamics.internship.userservice.component.processor.RequestEntityProcessor;
import com.griddynamics.internship.userservice.exception.NonExistentDataException;
import com.griddynamics.internship.userservice.model.card.CardTemplateDTO;
import com.griddynamics.internship.userservice.model.role.Role;
import com.griddynamics.internship.userservice.model.role.RoleTitle;
import com.griddynamics.internship.userservice.model.user.User;
import com.griddynamics.internship.userservice.model.user.UserDTO;
import com.griddynamics.internship.userservice.repo.RoleRepository;
import com.griddynamics.internship.userservice.repo.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

import static com.griddynamics.internship.userservice.utils.PageRequests.generatePageRequest;
import static com.griddynamics.internship.userservice.utils.ResponseMessages.ADMIN_NOT_FOUND;

@Slf4j
@Service
public class AdminService {
    @Autowired
    private JwtProcessor jwtProcessor;
    @Autowired
    private ResponseMapper mapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private RequestEntityProcessor entityProcessor;
    @Value("${card-service.url.card-templates}")
    private String templatesUrl;

    public Page<UserDTO> findAll(int pageNum, int pageSize) {
        PageRequest pageRequest = generatePageRequest(pageNum, pageSize);
        Role adminRole = roleRepository.findByTitle(RoleTitle.ROLE_ADMIN);
        Page<User> allAdmins = userRepository.findAllByRole(pageRequest, adminRole);

        return new UserPage(
                mapper.usersToDTO(allAdmins.getContent()),
                pageRequest,
                allAdmins.getTotalElements()
        );
    }

    public UserDTO findAdmin(int id) {
        Optional<User> admin = userRepository.findByIdAndRole(
                id, roleRepository.findByTitle(RoleTitle.ROLE_ADMIN));

        return admin
                .map(UserDTO::new)
                .orElseThrow(() -> new NonExistentDataException(ADMIN_NOT_FOUND));
    }

    public Page<CardTemplateDTO> findCardTemplates(int id) {
        if(!userRepository.existsByIdAndRole(id, roleRepository.findByTitle(RoleTitle.ROLE_ADMIN)))
            throw new NonExistentDataException(ADMIN_NOT_FOUND);

        HttpEntity<String> httpEntity = entityProcessor
                .generateEntityForCurrUserAuthorization();

        log.info("Card service url = " + templatesUrl);
        List<CardTemplateDTO> templates = restTemplate.exchange(
                templatesUrl,
                HttpMethod.GET,
                httpEntity,
                CardTemplatePage.class
        ).getBody().getContent();
        log.info("Templates for user with id {} are: {}", id, templates);

        return new CardTemplatePage(templates.stream().filter(el -> el.createdById() == id).toList());
    }
}
