package com.monapp.profilapp.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.*;

public record LoginRequest(
        @NotBlank(message = "L'email est obligatoire")
        @Email(message = "L'email est correcte")
        String email,

        @NotBlank(message = "Le mot de passe est obligatoire")
        String password
) {}
