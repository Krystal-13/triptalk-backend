package com.zero.triptalk.exception.custom;

import lombok.Getter;

@Getter
public class OauthFeignException extends RuntimeException{

    private final int status;
    private final String errorMessage;

    public OauthFeignException(int status, String errorMessage) {
        this.status = status;
        this.errorMessage = errorMessage;
    }
}
