package com.example.tourguideinkorea.oauth2.service;

import com.example.tourguideinkorea.oauth2.entity.*;
import com.example.tourguideinkorea.oauth2.repository.UserRepository;
import com.example.tourguideinkorea.oauth2.util.GoogleOAuth2Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OAuth2Service {
    private final GoogleOAuth2Util googleOAuth2Util;
    private final UserRepository userRepository;

    public String getLoginUrl() {
        return googleOAuth2Util.getRedirectUrl();
    }

    public GoogleOAuth2Token getGoogleAccessToken(String code) throws Exception {
        return googleOAuth2Util.getAccessToken(code);
    }

    public GoogleUser getGoogleUserInfo(GoogleOAuth2Token googleOAuth2Token) throws Exception {
        return googleOAuth2Util.getUserInfo(googleOAuth2Token);
    }

    public User findUser(String id) {
        return userRepository.findById(id).orElse(null);
    }

    public User saveUser(GoogleUser googleUser) {
        return userRepository.save(googleUser.toUser());
    }
}
