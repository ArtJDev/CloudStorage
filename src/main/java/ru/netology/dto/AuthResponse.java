package ru.netology.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class AuthResponse {
    private String header;
    private String token;

    public AuthResponse(String token) {
        this.header = "auth-token";
        this.token = token;
    }

}
