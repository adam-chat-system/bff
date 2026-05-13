package com.chatapp.bff.DTO.requests;

import lombok.Data;

@Data
public class LoginRequestDTO {
    private String username;
    private String password;
}