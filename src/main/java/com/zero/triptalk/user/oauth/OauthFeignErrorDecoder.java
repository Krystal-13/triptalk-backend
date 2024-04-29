package com.zero.triptalk.user.oauth;

import com.zero.triptalk.exception.custom.OauthFeignException;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class OauthFeignErrorDecoder implements ErrorDecoder {

    @Override
    public OauthFeignException decode(String methodKey, Response response) {

        String exceptionMessage = response.headers().get("www-authenticate").toString();
        Pattern pattern = Pattern.compile("error_description=\"(.*?)\"");
        Matcher matcher = pattern.matcher(exceptionMessage);

        if (matcher.find()) {
            String errorDescription = matcher.group(1);
            return new OauthFeignException(response.status(), errorDescription);
        } else {
            return new OauthFeignException(response.status(), response.reason());
        }
    }
}
