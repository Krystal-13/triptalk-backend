package com.zero.triptalk.user.oauth.service;

import com.zero.triptalk.config.JwtService;
import com.zero.triptalk.exception.code.UserErrorCode;
import com.zero.triptalk.exception.custom.UserException;
import com.zero.triptalk.user.entity.UserEntity;
import com.zero.triptalk.user.enumType.UserType;
import com.zero.triptalk.user.oauth.dto.SocialAuthResponse;
import com.zero.triptalk.user.oauth.dto.UserInfoResponse;
import com.zero.triptalk.user.repository.UserRepository;
import com.zero.triptalk.user.response.AuthenticationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    @Value("${cloud.aws.image}")
    private String profile;

    private final List<SocialLoginService> socialLoginServiceList;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    public String getSocialLoginUri(String loginType) {

        SocialLoginService socialLoginService = this.getSocialLoginService(loginType);

        return socialLoginService.getServiceUri(loginType);
    }

    public AuthenticationResponse doSocialLogin(String loginType, String code) {

        SocialLoginService socialLoginService = this.getSocialLoginService(loginType);

        SocialAuthResponse accessToken = socialLoginService.getAccessToken(code);

        UserInfoResponse userInfo = socialLoginService.getUserInfo(accessToken.getAccess_token());

        Optional<UserEntity> optionalUser = userRepository.findByEmail(userInfo.getEmail());
        UserEntity user = optionalUser.orElseGet(() -> this.saveUser(userInfo, loginType));

        return AuthenticationResponse.builder()
                .token(jwtService.generateToken(user))
                .build();
    }

    private SocialLoginService getSocialLoginService(String loginType) {

        return socialLoginServiceList.stream()
                                    .filter(x -> x.getServiceName().toString().equals(loginType.toUpperCase()))
                                    .findFirst()
                                    .orElseThrow(() -> new UserException(UserErrorCode.INVALID_REQUEST));
    }

    private UserEntity saveUser(UserInfoResponse userInfo, String loginType) {

        String uuid = UUID.randomUUID().toString().substring(0,8);
        LocalDateTime now = LocalDateTime.now();

        UserEntity user = UserEntity.builder()
                .UserType(UserType.USER)
                .name(userInfo.getName())
                .profile(profile)
                .email(userInfo.getEmail())
                .nickname(loginType + uuid)
                .aboutMe(loginType + uuid + " 님 안녕하세요. 자신을 소개해 주세요!")
                .password(BCrypt.hashpw(uuid, BCrypt.gensalt()))
                .registerAt(now)
                .updateAt(now)
                .build();

        userRepository.saveAndFlush(user);

        return user;
    }


}
