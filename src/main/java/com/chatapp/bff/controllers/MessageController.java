package com.chatapp.bff.controllers;
import reactor.core.publisher.Mono;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import com.chatapp.bff.DTO.requests.MessageRequestDTO;
import com.chatapp.bff.DTO.responses.MessageResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeoutException;

@RestController
@RequestMapping("/messages")
public class MessageController {

    private final WebClient webClient;
    public MessageController(WebClient.Builder builder,
                             @Value("${message.service.url}") String messageServiceUrl) {
        this.webClient = builder.baseUrl(messageServiceUrl).build();
    }

    @PostMapping
    public void sendMessage(@RequestBody MessageRequestDTO request){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated() || auth.getName().equals("anonymousUser")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not authenticated");
        }

        String username = auth.getName();
        request.setSender(username);

        blockWithGatewayHandling(
                webClient.post()
                        .uri("/messages")
                        .bodyValue(request)
                        .retrieve()
                        .onStatus(status -> status.is4xxClientError(), response ->
                                response.bodyToMono(String.class)
                                        .defaultIfEmpty("Upstream client error")
                                        .map(error -> {
                                            return new ResponseStatusException(HttpStatus.BAD_REQUEST, "Upstream client error");
                                        })
                        )
                        .onStatus(status -> status.is5xxServerError(), response ->
                                response.bodyToMono(String.class)
                                        .defaultIfEmpty("Upstream server error")
                                        .map(error -> {
                                            return new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Message service error");
                                        })
                        )
                        .bodyToMono(Void.class)
        );
    }


    @GetMapping
    public List<MessageResponseDTO> getAllMessages(){

        return blockWithGatewayHandling(
                webClient.get()
                        .uri("/messages")
                        .retrieve()
                        .onStatus(status -> status.is4xxClientError(), response ->
                                response.bodyToMono(String.class)
                                        .defaultIfEmpty("Upstream client error")
                                        .map(error -> {
                                            return new ResponseStatusException(HttpStatus.BAD_REQUEST, "Upstream client error");
                                        })
                        )
                        .onStatus(status -> status.is5xxServerError(), response ->
                                response.bodyToMono(String.class)
                                        .defaultIfEmpty("Upstream server error")
                                        .map(error -> {
                                            return new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Message service error");
                                        })
                        )
                        .bodyToMono(new ParameterizedTypeReference<List<MessageResponseDTO>>() {})
        );
    }

    @GetMapping("/sender/{sender}")
    public List<MessageResponseDTO> getBySender(@PathVariable String sender){

        return blockWithGatewayHandling(
                webClient.get()
                        .uri("/messages/sender/{sender}", sender)
                        .retrieve()
                        .onStatus(status -> status.is4xxClientError(), response ->
                                response.bodyToMono(String.class)
                                        .defaultIfEmpty("Upstream client error")
                                        .map(error -> {
                                            return new ResponseStatusException(HttpStatus.BAD_REQUEST, "Upstream client error");
                                        })
                        )
                        .onStatus(status -> status.is5xxServerError(), response ->
                                response.bodyToMono(String.class)
                                        .defaultIfEmpty("Upstream server error")
                                        .map(error -> {
                                            return new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Message service error");
                                        })
                        )
                        .bodyToMono(new ParameterizedTypeReference<List<MessageResponseDTO>>() {})
        );
    }

    @GetMapping("/my")
    public List<MessageResponseDTO> getMyMessages(){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated() || auth.getName().equals("anonymousUser")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not authenticated");
        }

        String username = auth.getName();

        return blockWithGatewayHandling(
                webClient.get()
                        .uri("/messages/sender/{sender}", username)
                        .retrieve()
                        .onStatus(status -> status.is4xxClientError(), response ->
                                response.bodyToMono(String.class)
                                        .defaultIfEmpty("Upstream client error")
                                        .map(error -> {
                                            return new ResponseStatusException(HttpStatus.BAD_REQUEST, "Upstream client error");
                                        })
                        )
                        .onStatus(status -> status.is5xxServerError(), response ->
                                response.bodyToMono(String.class)
                                        .defaultIfEmpty("Upstream server error")
                                        .map(error -> {
                                            return new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Message service error");
                                        })
                        )
                        .bodyToMono(new ParameterizedTypeReference<List<MessageResponseDTO>>() {})
        );
    }

    private <T> T blockWithGatewayHandling(Mono<T> mono) {
        return mono
                .timeout(Duration.ofSeconds(5))
                .onErrorMap(WebClientRequestException.class, ex ->
                        new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Message service unavailable")
                )
                .onErrorMap(TimeoutException.class, ex ->
                        new ResponseStatusException(HttpStatus.GATEWAY_TIMEOUT, "Message service timeout")
                )
                .block();
    }

}
