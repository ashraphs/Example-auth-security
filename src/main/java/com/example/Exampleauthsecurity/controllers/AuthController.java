package com.example.Exampleauthsecurity.controllers;

import com.example.Exampleauthsecurity.entities.User;
import com.example.Exampleauthsecurity.entities.role.Role;
import com.example.Exampleauthsecurity.entities.role.RoleName;
import com.example.Exampleauthsecurity.errors.ApiResponse;
import com.example.Exampleauthsecurity.errors.AppException;
import com.example.Exampleauthsecurity.payloads.AuthenticationResponse;
import com.example.Exampleauthsecurity.payloads.LoginRequest;
import com.example.Exampleauthsecurity.payloads.SignUpRequest;
import com.example.Exampleauthsecurity.repositories.RoleRepository;
import com.example.Exampleauthsecurity.repositories.UserRepository;
import com.example.Exampleauthsecurity.securities.TokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Collections;

@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @PostMapping("/signin")
    public ResponseEntity<?> signIn(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsernameOrEmail(),
                        loginRequest.getPassword()
                )
        );
        log.trace("Auth: {}", authentication);


        log.info("Auth Authorities: {}", authentication.getAuthorities());

        String token = tokenProvider.generateToken(authentication);
        log.info("Token generated: {}", token);

        return ResponseEntity.ok(new AuthenticationResponse(token));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@Valid @RequestBody SignUpRequest signUp) {

        if (userRepository.existsByUsername(signUp.getUsername()))
            return new ResponseEntity(new ApiResponse(false, "Username is already taken!"), HttpStatus.BAD_REQUEST);

        if (userRepository.existsByEmail(signUp.getEmail()))
            return new ResponseEntity(new ApiResponse(false, "Email is already taken!"), HttpStatus.BAD_REQUEST);

        User newUser = new User();
        newUser.setUsername(signUp.getUsername());
        newUser.setEmail(signUp.getEmail());
        newUser.setName(signUp.getName());
        newUser.setPassword(passwordEncoder.encode(signUp.getPassword()));

        Role userRole = roleRepository.findByName(RoleName.ROLE_USER).orElseThrow(() -> new AppException("User Role not set."));

        log.info("Role: ", userRole);
        newUser.setRoles(Collections.singleton(userRole));

        newUser = userRepository.save(newUser);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/users/{username}")
                .buildAndExpand(newUser.getUsername()).toUri();

        return ResponseEntity.created(location).body(new ApiResponse(true, "User registered successfully"));

    }


}
