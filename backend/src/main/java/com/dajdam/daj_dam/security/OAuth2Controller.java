package com.dajdam.daj_dam.security;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@RestController
@RequestMapping("/oauth2")
public class OAuth2Controller {

//    @GetMapping("/callback")
//    public ResponseEntity<String> callback(@AuthenticationPrincipal OAuth2AuthenticationToken authentication) {
//        if (authentication == null) {
//            System.err.println("Authentication token is null.");
//            return ResponseEntity.badRequest().body("No authentication present. Please start via /oauth2/authorization/google");
//        }
//
//        Map<String, Object> attributes = authentication.getPrincipal().getAttributes();
//        System.out.println("User Attributes: " + attributes);
//
//        String userJson = URLEncoder.encode(attributes.toString(), StandardCharsets.UTF_8);
//
//        return ResponseEntity.status(302)
//                .header(HttpHeaders.LOCATION, "http://localhost:5173/login?userInfo=" + userJson)
//                .build();
//    }
}

