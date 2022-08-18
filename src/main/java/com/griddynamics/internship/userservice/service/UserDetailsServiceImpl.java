package com.griddynamics.internship.userservice.service;

import com.griddynamics.internship.userservice.model.User;
import com.griddynamics.internship.userservice.model.UserWrapper;
import com.griddynamics.internship.userservice.datasource.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username);
        if (user == null) throw new UsernameNotFoundException("user not found");
        return new UserWrapper(user);
    }
}
