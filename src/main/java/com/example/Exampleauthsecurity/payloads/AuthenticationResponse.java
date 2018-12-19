package com.example.Exampleauthsecurity.payloads;

import lombok.Data;

@Data
public class AuthenticationResponse {

    private String accessToken;
    private String tokenType = "Bearer";

    public AuthenticationResponse(String accessToken) {
        this.accessToken = accessToken;
    }
}
