package com.monapp.profilapp.dto;

public record AuthResponse(String token, String type, UserResponse user) {
}
