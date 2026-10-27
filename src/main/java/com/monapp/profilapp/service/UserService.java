package com.monapp.profilapp.service;

import com.monapp.profilapp.dto.UpdateProfileRequest;
import com.monapp.profilapp.dto.UserResponse;
import com.monapp.profilapp.entity.User;
import com.monapp.profilapp.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;

@Service
@Setter
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private  final UserMapper userMapper;

    public UserResponse getMyProfile(String email){
        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Utilisateur introuvable"));

        return userMapper.toResponse(user);
    }

    public UserResponse updateMyProfile(String email, UpdateProfileRequest request){
        //on charge l'utilisateur connecté(identifié par son token
        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Utilisateur introuvable"));
        user.setFirstname(request.firstname());
        user.setLastname(request.lastname());

        User saved = userRepository.save(user);
        return userMapper.toResponse(saved);
    }

    //tout kes utilisateurs.
    public List<UserResponse> getAllUsers(){
        return userRepository.findAll().stream()
                .map(userMapper::toResponse)
                .toList();
    }

    public UserResponse getUserById(Long id){
        User user = userRepository.findById(id)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Utilisateur non trouvé"));
        return userMapper.toResponse(user);
    }

    public  void deleteUser(Long id){

        if (!userRepository.existsById(id)){
            throw  new ResponseStatusException(HttpStatus.NOT_FOUND, "Utilisateur non trouvé");
        }
       userRepository.deleteById(id);
    }

}
