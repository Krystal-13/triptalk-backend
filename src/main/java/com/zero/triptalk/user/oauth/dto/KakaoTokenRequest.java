package com.zero.triptalk.user.oauth.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class KakaoTokenRequest {
    private String serviceUri;
    private String clientId;
    private String redirectUri;
    private String code;
    private String clientSecret;

    @Builder
    public KakaoTokenRequest(String serviceUri, String clientId, String redirectUri, String code, String clientSecret) {
        this.serviceUri = serviceUri;
        this.clientId = clientId;
        this.redirectUri = redirectUri;
        this.code = code;
        this.clientSecret = clientSecret;
    }

    @Override
    public String toString() {

        return "client_id=" + clientId + '&' +
                "redirect_uri=" + redirectUri + '&' +
                "code=" + code + '&' +
                "grant_type=authorization_code" + '&' +
                "client_secret=" + clientSecret;
    }

    public String getServiceUri() {

        return serviceUri
                + "?response_type=code"
                + "&client_id=" + clientId
                + "&redirect_uri=" + redirectUri;
    }
}
