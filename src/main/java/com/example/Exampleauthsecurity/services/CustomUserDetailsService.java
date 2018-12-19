package com.example.Exampleauthsecurity.services;

import com.example.Exampleauthsecurity.entities.User;
import com.example.Exampleauthsecurity.repositories.UserRepository;
import com.example.Exampleauthsecurity.securities.UserPrincipal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Slf4j
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    /**
     * Let user login with either username or email
     *
     * @param usernameOrEmail - Let user login using username or email
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {

        User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail).orElseThrow(() ->
                new UsernameNotFoundException("User not found with username or email"));

        log.info("Load user by username: {}", user);

        return UserPrincipal.create(user);

    }

    @Transactional
    public UserDetails loadUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException()
        );
        log.info("Load user by Id: {}", user);

        return UserPrincipal.create(user);

    }
}
