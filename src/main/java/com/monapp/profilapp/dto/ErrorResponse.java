package com.monapp.profilapp.dto;


import java.time.LocalDateTime;
import java.util.Map;

public record ErrorResponse(
        int status, //code http
        String message,  //message d'erreur
        Map<String, String> errors,  //erreurs par champ
        LocalDateTime timestamp   //quand l'erreur s'est deroulé
) {

}
