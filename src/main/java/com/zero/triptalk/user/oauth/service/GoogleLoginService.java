package com.zero.triptalk.user.oauth.service;

import com.google.gson.Gson;
import com.zero.triptalk.user.enumType.LoginType;
import com.zero.triptalk.user.oauth.client.FeignClientGoogleAuth;
import com.zero.triptalk.user.oauth.client.FeignClientGoogleUser;
import com.zero.triptalk.user.oauth.dto.GoogleTokenRequest;
import com.zero.triptalk.user.oauth.dto.SocialAuthResponse;
import com.zero.triptalk.user.oauth.dto.UserInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GoogleLoginService implements SocialLoginService {
    @Value("${feign.client.google.service}")
    private String serviceUri;
    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String googleClientId;
    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String googleClientPw;
    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String redirectUri;
    @Value("${spring.security.oauth2.client.registration.google.authorization-grant-type}")
    private String grantType;
    @Value("${spring.security.oauth2.client.registration.google.scope}")
    private String scope;

    private final FeignClientGoogleAuth feignClientGoogleAuth;
    private final FeignClientGoogleUser feignClientGoogleUser;

    @Override
    public LoginType getServiceName() {
        return LoginType.GOOGLE;
    }

    @Override
    public String getServiceUri(String loginType) {

        GoogleTokenRequest googleTokenRequest = GoogleTokenRequest.builder()
                                                                .serviceUri(serviceUri)
                                                                .clientId(googleClientId)
                                                                .redirectUri(redirectUri)
                                                                .scope(scope)
                                                                .build();

        return googleTokenRequest.getServiceUri();
    }

    @Override
    public SocialAuthResponse getAccessToken(String code) {

        GoogleTokenRequest googleTokenRequest = GoogleTokenRequest.builder()
                                                                .clientId(googleClientId)
                                                                .clientSecret(googleClientPw)
                                                                .code(code)
                                                                .grantType(grantType)
                                                                .redirectUri(redirectUri)
                                                                .build();

        ResponseEntity<String> accessToken = feignClientGoogleAuth.getAccessToken(googleTokenRequest);

        Gson gson = new Gson();

        return gson.fromJson(accessToken.getBody(), SocialAuthResponse.class);
    }

    @Override
    public UserInfoResponse getUserInfo(String accessToken) {

        ResponseEntity<String> userInfo = feignClientGoogleUser.getUserInfo(accessToken);

        Gson gson = new Gson();

        return gson.fromJson(userInfo.getBody(), UserInfoResponse.class);
    }

}
