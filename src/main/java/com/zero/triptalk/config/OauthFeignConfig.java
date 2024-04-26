package com.zero.triptalk.config;

import com.zero.triptalk.user.oauth.OauthFeignErrorDecoder;
import feign.Logger;
import org.springframework.context.annotation.Bean;



public class OauthFeignConfig {
    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    @Bean
    public OauthFeignErrorDecoder oauthFeignErrorDecoder() {
        return new OauthFeignErrorDecoder();
    }
}
