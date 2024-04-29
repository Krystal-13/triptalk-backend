package com.zero.triptalk.user.oauth.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class GoogleTokenRequest {

    private String serviceUri;
    private String clientId;
    private String clientSecret;
    private String redirectUri;
    private String grantType;
    private String code;
    private String scope;

    @Builder
    public GoogleTokenRequest(String serviceUri, String clientId, String clientSecret, String redirectUri, String grantType, String code, String scope) {
        this.serviceUri = serviceUri;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.redirectUri = redirectUri;
        this.grantType = grantType;
        this.code = code;
        this.scope = scope;
    }

    public String getServiceUri() {

        return serviceUri
                + "?client_id=" + clientId
                + "&redirect_uri=" + redirectUri
                + "&response_type=code"
                + "&scope=" + scope;
    }
}
