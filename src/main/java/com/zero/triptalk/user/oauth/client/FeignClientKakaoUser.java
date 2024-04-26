package com.zero.triptalk.user.oauth.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(value = "kakaoUser", url="${feign.client.kakao.user}")
public interface FeignClientKakaoUser {
    @GetMapping
    ResponseEntity<String> getUserInfo(@RequestHeader(name = "Authorization") String accessToken);
}
