package com.example.Exampleauthsecurity;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class ExampleAuthSecurityApplicationTests {

    @Autowired
    AuthenticationManager authenticationManager;


    @Test
    public void passwordEncodeDecode() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        String passwordEncode = encoder.encode("Test");
        log.debug("Password Encode {} " + passwordEncode);

        String passwordUser = "Test";
        Boolean isMatch = encoder.matches(passwordEncode, passwordUser);
        log.debug("Is password match: {}" + isMatch);

    }

    @Test
    public void authenticationTest() {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        "amir12345", "amir12345"
                )
        );

        log.info(authentication.getAuthorities().toString());
    }
}
