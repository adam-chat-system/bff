package com.chatapp.bff.DTO.requests;

import lombok.*;

@Getter
@Setter
@ToString(exclude = "password")
@EqualsAndHashCode(exclude = "password")
public class LoginRequestDTO {
    private String username;
    private String password;
}