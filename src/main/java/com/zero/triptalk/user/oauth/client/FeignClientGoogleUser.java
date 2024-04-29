package com.zero.triptalk.user.oauth.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "googleUser", url="${feign.client.google.user}")
public interface FeignClientGoogleUser {
    @GetMapping
    ResponseEntity<String> getUserInfo(@RequestParam("access_token") String accessToken);
}
