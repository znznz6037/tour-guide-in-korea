package com.example.tourguideinkorea.oauth2.controller;

import com.example.tourguideinkorea.oauth2.entity.GoogleOAuth2Token;
import com.example.tourguideinkorea.oauth2.entity.GoogleUser;
import com.example.tourguideinkorea.oauth2.jwt.util.JwtUtil;
import com.example.tourguideinkorea.oauth2.service.OAuth2Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.userdetails.User;

import java.util.Collections;

@Slf4j
@RestController
@RequestMapping("")
@RequiredArgsConstructor
public class AuthController {
    private final OAuth2Service oAuth2Service;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtUtil jwtUtil;

    @GetMapping("/oauth2/profile")
    public com.example.tourguideinkorea.oauth2.entity.User.Response getUserInfo(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        com.example.tourguideinkorea.oauth2.entity.User oauth2User =  oAuth2Service.findUser(user.getUsername());

        return new com.example.tourguideinkorea.oauth2.entity.User.Response(oauth2User);
    }

    @GetMapping("/oauth2/login")
    public String oauth2UrlRequest() {
        return oAuth2Service.getLoginUrl();
    }

    @GetMapping("/login/oauth2/code/google/callback")
    public ResponseEntity<?> oauth2Callback(@RequestParam(name = "code") String code) throws Exception {
        GoogleOAuth2Token googleOAuth2Token = oAuth2Service.getGoogleAccessToken(code);
        GoogleUser googleUser = oAuth2Service.getGoogleUserInfo(googleOAuth2Token);
        oAuth2Service.saveUser(googleUser);

        try {
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(googleUser.getId(), googleUser.getEmail());
            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtil.createToken(authentication);

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("Location", "http://localhost:3000/login/success/" + jwt);
            return new ResponseEntity<>(httpHeaders, HttpStatus.FOUND);
        } catch(AuthenticationException ae) {
            log.info("Authentication exception trace: {}", ae);
            return new ResponseEntity<>(Collections.singletonMap("AuthenticationMap", ae.getLocalizedMessage()), HttpStatus.UNAUTHORIZED);
        }
    }
}