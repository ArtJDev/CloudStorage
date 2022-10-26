package ru.netology.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.netology.dto.AuthRequest;
import ru.netology.dto.AuthResponse;
import ru.netology.services.AuthService;

@RestController
@RequestMapping("/")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login (@RequestBody AuthRequest authRequest) {
        String token = authService.loginUser(authRequest);
        return token != null ? new ResponseEntity<>(new AuthResponse(token), HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
}