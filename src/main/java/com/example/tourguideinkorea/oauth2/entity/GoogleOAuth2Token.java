package com.example.tourguideinkorea.oauth2.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class GoogleOAuth2Token {
    private String access_token;
    private int expires_in;
    private String scope;
    private String token_type;
    private String id_token;
}
