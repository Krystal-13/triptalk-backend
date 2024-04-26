package com.zero.triptalk.user.oauth.service;

import com.zero.triptalk.user.enumType.LoginType;
import com.zero.triptalk.user.oauth.dto.SocialAuthResponse;
import com.zero.triptalk.user.oauth.dto.UserInfoResponse;
import org.springframework.stereotype.Service;

@Service
public interface SocialLoginService {
    LoginType getServiceName();
    String getServiceUri(String loginType);
    SocialAuthResponse getAccessToken(String code);
    UserInfoResponse getUserInfo(String accessToken);
}
