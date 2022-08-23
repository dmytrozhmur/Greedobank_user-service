package com.griddynamics.internship.userservice.datasource.repo;

import com.griddynamics.internship.userservice.UserServiceApplication;
import com.griddynamics.internship.userservice.model.Role;
import com.griddynamics.internship.userservice.model.RoleTitle;
import com.griddynamics.internship.userservice.model.User;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@DataJpaTest
@ContextConfiguration(classes = UserServiceApplication.class)
@ComponentScan(basePackages = "com.griddynamics.internship.userservice")
public class UserRepositoryTest {
    public static final String TEST_EMAIL = "dmytro.zhmur@nure.ua";
    @Autowired
    private UserRepository userRepository;

    @Test
    void findByEmail() {
        User user = createUser();
        userRepository.save(user);

        User savedUser = userRepository.findByEmail(TEST_EMAIL);

        assertEquals(user, savedUser);
    }

    private User createUser() {
        return new User(
                "Dmytro",
                "Zhmur",
                TEST_EMAIL,
                "password",
                new Role(1, RoleTitle.defaultTitle())
        );
    }
}