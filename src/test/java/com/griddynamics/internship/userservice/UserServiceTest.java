package com.griddynamics.internship.userservice;

import com.griddynamics.internship.userservice.model.User;
import com.griddynamics.internship.userservice.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.hamcrest.MatcherAssert.assertThat;


@SpringBootTest
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @Test
    public void getAllUsers() {
        Collection<User> actual = getActual();
        Collection<User> expected = getExpected();

        assertThat(actual, is(expected));
        assertThat(actual, hasSize(5));
    }

    private Collection<User> getActual() {
        Iterable<User> users = userService.findAll();
        Collection<User> result = new HashSet<>();

        users.iterator().forEachRemaining(result::add);

        return result;
    }

    private Collection<User> getExpected() {
        return new HashSet<>(Arrays.asList(
                new User(1, "Dmytro", "Zhmur", "dzhmur@griddynamics.com", "aboba18"),
                new User(2, "Yevheniia", "Komiahina", "ykomiahina@griddynamics.com", "password1"),
                new User(3, "Oleksandr", "Kukurik", "okukurik@girddynamics.com", "password2"),
                new User(4, "Iryna", "Subota", "isubota@griddynamics.com", "password3"),
                new User(5, "Tetiana", "Komarova", "tkomarova@griddynamics.com", "password4")
        ));
    }
}
