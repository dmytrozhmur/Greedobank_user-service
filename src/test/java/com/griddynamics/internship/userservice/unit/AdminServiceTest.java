package com.griddynamics.internship.userservice.unit;

import com.griddynamics.internship.userservice.model.role.Role;
import com.griddynamics.internship.userservice.model.role.RoleTitle;
import com.griddynamics.internship.userservice.model.user.UserDTO;
import com.griddynamics.internship.userservice.service.AdminService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;

import java.util.Collection;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AdminServiceTest extends UserServiceTest {
    @Autowired
    private AdminService adminService;

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
}
