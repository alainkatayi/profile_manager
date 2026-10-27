package com.monapp.profilapp.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "removed_token")
@Getter
@Setter@NoArgsConstructor
@AllArgsConstructor
@Builder


public class RevokedToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //id auto incrémenté
    private Long id;

    //le token revoqué
    @Column(nullable = false, unique = true, length = 1000)
    private String token;

    @Column(nullable = false)
    private LocalDateTime expiresAt;
}
