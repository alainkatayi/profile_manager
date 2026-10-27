package com.monapp.profilapp.repository;
import com.monapp.profilapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


//fait l'access à la base de données
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User>findByEmail(String email);
    boolean existsByEmail(String email);
}
