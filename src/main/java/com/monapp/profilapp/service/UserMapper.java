package com.monapp.profilapp.service;

import com.monapp.profilapp.dto.UserResponse;
import com.monapp.profilapp.entity.User;
import org.springframework.stereotype.Component;


// @Component : Spring crée cet objet et le garde disponible pour l'injection.
// On utilise @Component (et pas @Service) car il n'y a pas de logique métier,
// juste un outil technique de conversion.

@Component
public class UserMapper {

    // Entrée : l'entité complète (avec le hash du mot de passe)
    // Sortie : le DTO, qui ne contient QUE ce qu'on accepte de montrer
    public UserResponse toResponse(User user){
        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getFirstname(),
                user.getLastname(),
                user.getProfileImage(),
                user.getRole(),
                user.getCreatedAt()
        );
    }


}
