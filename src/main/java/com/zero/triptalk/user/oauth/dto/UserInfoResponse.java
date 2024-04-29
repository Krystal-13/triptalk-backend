package com.zero.triptalk.user.oauth.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserInfoResponse {
    private String id;
    private String email;
    private String name;

    @Builder
    public UserInfoResponse(String id, String email, String name) {
        this.id = id;
        this.email = email;
        this.name = name;
    }
}
