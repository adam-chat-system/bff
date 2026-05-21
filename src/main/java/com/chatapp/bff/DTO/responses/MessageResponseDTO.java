package com.chatapp.bff.DTO.responses;

import java.time.LocalDateTime;

public class MessageResponseDTO {
    private String sender;
    private String content;
    private LocalDateTime timestamp;

    public String getSender() {
        return sender;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
