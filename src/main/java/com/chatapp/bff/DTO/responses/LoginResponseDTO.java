package com.chatapp.bff.DTO.responses;

import lombok.Data;
import lombok.ToString;

@Data
@ToString(exclude = "token")
public class LoginResponseDTO {
    private String token;
}