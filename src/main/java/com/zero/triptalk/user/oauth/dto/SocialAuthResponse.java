package com.zero.triptalk.user.oauth.dto;

import lombok.Getter;

@Getter
public class SocialAuthResponse {
    private String access_token;
    private String expires_in;
    private String refresh_token;
    private String scope;
    private String token_type;
    private String id_token;
}
