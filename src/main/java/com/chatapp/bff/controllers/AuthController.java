package com.chatapp.bff.controllers;

import com.chatapp.bff.DTO.requests.LoginRequestDTO;
import com.chatapp.bff.DTO.responses.LoginResponseDTO;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final WebClient webClient;

    public AuthController(WebClient.Builder builder, @Value("${auth.service.url}") String authServiceUrl) {
        this.webClient = builder.baseUrl(authServiceUrl).build();
    }

    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody LoginRequestDTO request) {

        return webClient.post()
                .uri("/auth/login")
                .bodyValue(request)
                .retrieve()
                .onStatus(status -> status.is4xxClientError(), response ->
                        response.bodyToMono(String.class)
                                .map(error -> new RuntimeException("Unauthorized: " + error))
                )
                .onStatus(status -> status.is5xxServerError(), response ->
                        response.bodyToMono(String.class)
                                .map(error -> new RuntimeException("Auth service error: " + error))
                )
                .bodyToMono(LoginResponseDTO.class)
                .block();
    }
    
    @GetMapping("/me")
    public String me() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        return auth.getName();
    }
}
