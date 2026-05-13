package com.chatapp.bff.controllers;

import com.chatapp.bff.DTO.requests.LoginRequestDTO;
import com.chatapp.bff.DTO.responses.LoginResponseDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.beans.factory.annotation.Value;
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
                .bodyToMono(LoginResponseDTO.class)
                .block();
    }
}
