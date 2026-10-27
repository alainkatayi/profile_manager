package com.monapp.profilapp.dto;
import jakarta.validation.constraints.*;

//objet d'entrée sortie, type formualire quoi
public record RegisterRequest(
        @NotBlank(message = "L'email est obligatoire")
        @Email(message = "Email Invalide")
        String email,

        @NotBlank(message = "Le mot de passe est obligatoire")
        @Size(min = 8, message = "Le mot de passe doit contenir au moins 8 caractere")
        String password,

        @NotBlank(message = "Le prénom est obligatoire")
        String firstname,

        @NotBlank(message = "Le nom est obligatoire")
        String lastname
) {
}
