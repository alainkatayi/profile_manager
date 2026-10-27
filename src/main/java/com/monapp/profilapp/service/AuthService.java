package com.monapp.profilapp.service;
import com.monapp.profilapp.dto.AuthResponse;
import com.monapp.profilapp.dto.LoginRequest;
import com.monapp.profilapp.dto.RegisterRequest;
import com.monapp.profilapp.dto.UserResponse;
import com.monapp.profilapp.entity.RevokedToken;
import com.monapp.profilapp.entity.User;
import com.monapp.profilapp.repository.RevokedTokenRepository;
import com.monapp.profilapp.repository.UserRepository;
import com.monapp.profilapp.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.webmvc.autoconfigure.WebMvcProperties;
import org.springframework.http.HttpStatus;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;

@Service
@RequiredArgsConstructor

//fait les regles metiers
public class AuthService {
    private final   UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final RevokedTokenRepository revokedTokenRepository;
    private final UserMapper userMapper;

    private final JwtService jwtService;

    public UserResponse register(RegisterRequest request){
        if(userRepository.existsByEmail(request.email())){
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Cet email est deja utilisé");
        }
        User user = User.builder()
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .firstname(request.firstname())
                .lastname(request.lastname())
                .role("USER")
                .build();
        User saved = userRepository.save(user);
        return userMapper.toResponse(userRepository.save(user));
    }

    public AuthResponse login(LoginRequest request){
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.email(), request.password())
            );
        }catch (AuthenticationException e){
            throw  new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Email ou mot de passe incorrecte");
        }
        User user = userRepository.findByEmail(request.email()).orElseThrow();
        String token = jwtService.generateToken(user.getEmail(), user.getRole());

        return new AuthResponse(token, "Bearer",userMapper.toResponse(user));
    }

    public  void logout(String token){
        //si le token existe deja dans la liste, on ne fait rien
        if (revokedTokenRepository.existsByToken(token)){
            return;
        }

        // On lit la date d'expiration dans le token
        Date expiration = jwtService.extactExpiration(token);

        //on la convertit en local time, comme le type de notre table
        LocalDateTime expiresAt = expiration.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        // on enregistre le token dans la table noir
        RevokedToken revoked =  RevokedToken.builder()
                .token(token)
                .expiresAt(expiresAt)
                .build();
        revokedTokenRepository.save(revoked);

    }


}
