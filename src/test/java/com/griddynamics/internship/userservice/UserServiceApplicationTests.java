package com.griddynamics.internship.userservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.griddynamics.internship.userservice.controller.UserController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class UserServiceApplicationTests {
	@Autowired
	private UserServiceApplication application;
	@Autowired
	private ObjectMapper mapper;
	@Test
	void contextLoads() {
		assertThat(application).isNotNull();
		assertThat(mapper).isNotNull();
	}
}
