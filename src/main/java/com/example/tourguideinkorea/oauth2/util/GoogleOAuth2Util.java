package com.example.tourguideinkorea.oauth2.util;

import com.example.tourguideinkorea.oauth2.entity.GoogleOAuth2Token;
import com.example.tourguideinkorea.oauth2.entity.GoogleUser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class GoogleOAuth2Util {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.client.registration.google.scope}")
    private String scope;

    @Value("${spring.security.oauth2.client.registration.google.url}")
    private String url;

    @Value("${spring.security.oauth2.client.registration.google.redirect-url}")
    private String redirectUrl;

    @Value("${spring.security.oauth2.client.registration.google.access-token-url}")
    private String requestAccessTokenUrl;

    @Value("${spring.security.oauth2.client.registration.google.userinfo-url}")
    private String requestUserInfoUrl;

    public String getRedirectUrl() {
        Map<String,Object> params = new HashMap<>();

        params.put("scope", scope);
        params.put("response_type", "code");
        params.put("client_id", clientId);
        params.put("redirect_uri", redirectUrl);

        String parameterString = params.entrySet().stream()
                .map(x -> x.getKey() + "=" + x.getValue())
                .collect(Collectors.joining("&"));
        String redirectURL = url + "?" + parameterString;

        log.info("redirect_url={}", redirectURL);

        return redirectURL;
    }

    public GoogleOAuth2Token getAccessToken(String code) throws Exception {
        ObjectNode jsonNodes = JsonNodeFactory.instance.objectNode();
        jsonNodes.put("code", code);
        jsonNodes.put("client_id", clientId);
        jsonNodes.put("client_secret", clientSecret);
        jsonNodes.put("grant_type", "authorization_code");
        jsonNodes.put("redirect_uri", redirectUrl);

        ResponseEntity<JsonNode> postResult
                = restTemplate.postForEntity(requestAccessTokenUrl, jsonNodes, JsonNode.class);

        GoogleOAuth2Token googleOAuth2Token = new GoogleOAuth2Token(
                postResult.getBody().get("access_token").asText(),
                postResult.getBody().get("expires_in").asInt(),
                postResult.getBody().get("scope").asText(),
                postResult.getBody().get("token_type").asText(),
                postResult.getBody().get("id_token").asText()
        );

        return googleOAuth2Token;
    }

    public GoogleUser getUserInfo(GoogleOAuth2Token googleOAuth2Token) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();

        HttpEntity<MultiValueMap<String,String>> request = new HttpEntity<>(headers);
        headers.add("Authorization","Bearer " + googleOAuth2Token.getAccess_token());
        ResponseEntity<String> userInfoResponse = restTemplate.exchange(requestUserInfoUrl, HttpMethod.GET, request, String.class);

        return objectMapper.readValue(userInfoResponse.getBody(), GoogleUser.class);
    }
}