package com.monapp.profilapp.dto;
import java.time.LocalDateTime;

//definit la response pour un tilisateurs,
//avec record, le gettet et le setter sont automatique, pas besoin de les ecrires
public record UserResponse(
        Long Id,
        String Email,
        String firstname,
        String lastname,
        String profileImage,
        String role,
        LocalDateTime createdAt

) {
}
