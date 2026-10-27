package com.monapp.profilapp.controller;

import com.monapp.profilapp.dto.UpdateProfileRequest;
import com.monapp.profilapp.dto.UserResponse;
import com.monapp.profilapp.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/users") //prefixe de toute les routes de cette classe
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getMet(Authentication authentication){
        // Spring injecte l'objet Authentication tout seul :
        // c'est la "carte de connexion" déposée par notre filtre.
        // getName() retourne le username, donc l'email.

        String email = authentication.getName();
        return ResponseEntity.ok(userService.getMyProfile(email));
    }

    @PutMapping("/me")
    public ResponseEntity<UserResponse> updateMe(Authentication authentication, @Valid @RequestBody UpdateProfileRequest request){
        String email = authentication.getName();
        return ResponseEntity.ok(userService.updateMyProfile(email, request));

    }
}
