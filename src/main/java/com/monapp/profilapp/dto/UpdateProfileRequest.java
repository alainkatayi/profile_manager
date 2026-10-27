package com.monapp.profilapp.dto;

import jakarta.validation.constraints.NotBlank;


// Ce que le client a le droit d'envoyer pour modifier son profil.
// Pas de role, pas d'email, pas de password, pas d'id.
public record UpdateProfileRequest(
        @NotBlank(message = "Le prénom est obligatoire")
        String firstname,

        @NotBlank(message="Le nom est obligatoire")
        String lastname
) {

}
