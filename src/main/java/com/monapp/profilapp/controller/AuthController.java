package com.monapp.profilapp.controller;

import com.monapp.profilapp.dto.AuthResponse;
import com.monapp.profilapp.dto.LoginRequest;
import com.monapp.profilapp.dto.RegisterRequest;
import com.monapp.profilapp.dto.UserResponse;
import com.monapp.profilapp.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/auth")

public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest request){
        UserResponse response = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse>login(@Valid @RequestBody LoginRequest request){
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String header){
        //on retire le bear dans le header
        String token = header.substring(7);
        authService.logout(token);
        return ResponseEntity.noContent().build();
    }
}
