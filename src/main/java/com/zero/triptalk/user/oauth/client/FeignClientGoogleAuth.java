package com.zero.triptalk.user.oauth.client;

import com.zero.triptalk.user.oauth.dto.GoogleTokenRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "googleAuth", url="${feign.client.google.auth}")
public interface FeignClientGoogleAuth {
    @PostMapping
    ResponseEntity<String> getAccessToken(@RequestBody GoogleTokenRequest request);
}
