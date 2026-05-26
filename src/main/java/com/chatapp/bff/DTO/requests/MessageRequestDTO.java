package com.chatapp.bff.DTO.requests;

public class MessageRequestDTO {
    private String sender;
    private String content;

    public MessageRequestDTO() {}

    public MessageRequestDTO(String sender, String content) {
        this.sender = sender;
        this.content = content;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
