package com.zero.triptalk.user.oauth;

import com.zero.triptalk.user.oauth.service.UserService;
import com.zero.triptalk.user.response.AuthenticationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/oauth")
public class SocialLoginController {

    private final UserService userService;

    @GetMapping("/{provider}")
    public ResponseEntity<String> getSocialLoginUri(@PathVariable String provider) {

        return ResponseEntity.ok(userService.getSocialLoginUri(provider));
    }

    @GetMapping("/{provider}/callback")
    public ResponseEntity<AuthenticationResponse> doSocialLogin(@PathVariable String provider, @RequestParam("code") String code) {

        return ResponseEntity.ok(userService.doSocialLogin(provider, code));

    }

}
