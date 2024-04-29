package com.zero.triptalk.user.oauth.service;

import com.google.gson.Gson;
import com.zero.triptalk.user.enumType.LoginType;
import com.zero.triptalk.user.oauth.client.FeignClientKakaoAuth;
import com.zero.triptalk.user.oauth.client.FeignClientKakaoUser;
import com.zero.triptalk.user.oauth.dto.KakaoTokenRequest;
import com.zero.triptalk.user.oauth.dto.SocialAuthResponse;
import com.zero.triptalk.user.oauth.dto.UserInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KakaoLoginService implements SocialLoginService {
    @Value("${feign.client.kakao.service}")
    private String serviceUri;
    @Value("${kakao.client.id}")
    private String clientId;
    @Value("${kakao.redirect.url}")
    private String redirectUri;
    @Value("${kakao.client.secret}")
    private String clientSecret;

    private final FeignClientKakaoAuth feignClientKakaoAuth;
    private final FeignClientKakaoUser feignClientKakaoUser;

    @Override
    public LoginType getServiceName() {
        return LoginType.KAKAO;
    }

    @Override
    public String getServiceUri(String loginType) {

        KakaoTokenRequest kakaoTokenRequest = KakaoTokenRequest.builder()
                                                            .serviceUri(serviceUri)
                                                            .clientId(clientId)
                                                            .redirectUri(redirectUri)
                                                            .build();

        return kakaoTokenRequest.getServiceUri();
    }

    @Override
    public SocialAuthResponse getAccessToken(String code) {

        KakaoTokenRequest kakaoTokenRequest = KakaoTokenRequest.builder()
                                                                .clientId(clientId)
                                                                .code(code)
                                                                .redirectUri(redirectUri)
                                                                .clientSecret(clientSecret)
                                                                .build();

        ResponseEntity<String> accessToken = feignClientKakaoAuth.getAccessToken(kakaoTokenRequest.toString());

        Gson gson = new Gson();

        return gson.fromJson(accessToken.getBody(), SocialAuthResponse.class);
    }

    @Override
    public UserInfoResponse getUserInfo(String accessToken) {

        ResponseEntity<String> userInfo = feignClientKakaoUser.getUserInfo("Bearer " + accessToken);

        Gson gson = new Gson();

        return gson.fromJson(userInfo.getBody(), UserInfoResponse.class);
    }
}
