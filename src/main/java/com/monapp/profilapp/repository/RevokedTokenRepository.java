package com.monapp.profilapp.repository;

import com.monapp.profilapp.entity.RevokedToken;
import org.springframework.data.jpa.repository.JpaRepository;


// JpaRepository<RevokedToken, Long> :
//   - RevokedToken = l'entité (la table) qu'on manipule
//   - Long         = le type de son id
// On obtient gratuitement save(), findById(), findAll(), deleteById()
//Spring s'occupe du reste
public interface RevokedTokenRepository extends JpaRepository<RevokedToken, Long> {
    // Spring lit le NOM de la méthode et écrit la requête tout seul :
    //   "existsBy" + "Token" = "existe-t-il une ligne dont la colonne token vaut ... ?"
    // SQL généré : SELECT ... FROM revoked_tokens WHERE token = ?
    // Retourne true ou false.
    boolean existsByToken(String token);
}
