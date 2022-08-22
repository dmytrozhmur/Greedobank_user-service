package com.griddynamics.internship.userservice;

import com.griddynamics.internship.userservice.datasource.UserJpaConfiguration;
import com.griddynamics.internship.userservice.datasource.repo.UserRepository;
import com.griddynamics.internship.userservice.model.Role;
import com.griddynamics.internship.userservice.model.RoleTitle;
import com.griddynamics.internship.userservice.model.User;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class InMemoryDBTest {
    @Resource
    private UserRepository userRepository;

    @Test
    public void saveUserAndGetOk() {
        User user = new User(
                "Dmytro",
                "Zhmur",
                "dmytro.zhmur@nure.ua",
                "password",
                new Role(1, RoleTitle.ROLE_USER)
        );
        userRepository.save(user);

        User savedUser = userRepository.findByEmail("dmytro.zhmur@nure.ua");
        assertEquals(user.getFirstName(), savedUser.getFirstName());
    }
}
