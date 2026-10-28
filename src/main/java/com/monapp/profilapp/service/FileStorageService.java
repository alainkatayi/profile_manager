package com.monapp.profilapp.service;

import org.hibernate.dialect.function.HANARegexpLikeFunction;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
public class FileStorageService {
    private final static  List<String> ALLOWED_EXTENSIONS = List.of("jpg","png","jpeg", "webp");
    private final static List<String> ALLOWED_TYPES = List.of("image/jpeg","image/png","image/webp");

    //le dossier de stockage
    private final Path uploadDir;

    //constructeur: lit upload-dir, donc uploadDir
    public FileStorageService(@Value("${file.upload-dir}") String dir){
        this.uploadDir = Paths.get(dir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(uploadDir); //creer le dossier s'il n'existe pas
        }catch (IOException e){
            throw new IllegalStateException("Impossible de creer le dossier d'upload");
        }

    }

    //enregistrement: retourne le nom du fichier à stocker en base
    public String store(MultipartFile file){

        //fichier vide?
        if (file == null || file.isEmpty()  ){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Aucun fichier envoyé");
        }

        //c'est le bon type?
        if (!ALLOWED_TYPES.contains(file.getContentType())){
            throw  new ResponseStatusException(HttpStatus.BAD_REQUEST,"Format non valide(jpg, png, webp uniquement");
        }

        //nom d'origine
        String original = file.getOriginalFilename();
        String extension = "";
        if (original != null && original.contains(".")){
            extension = original.substring(original.lastIndexOf('.') +1).toLowerCase(Locale.ROOT);
        }

        if (!ALLOWED_EXTENSIONS.contains(extension)){
            throw  new ResponseStatusException(HttpStatus.BAD_REQUEST, "Extension non acceptée (jpg, png, webp uniquement)");
        }

        //nom sur du fichier UUID
        String fileName =  UUID.randomUUID() + "." + extension;

        //copie sur le disque
        try {
            Path target = uploadDir.resolve(fileName);
            Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
        }catch (IOException e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erreur lors de l'enregistrement du fichier");
        }
        return fileName;
    }

    //retrouver un fichier à partir de son nom
    public Resource load(String fileName){
        try {
            Path file = uploadDir.resolve(fileName).normalize();

            if (!file.startsWith(uploadDir)){
                throw  new ResponseStatusException(HttpStatus.BAD_REQUEST,"Le nom du fichier est invalide");
            }
            Resource resource = new UrlResource(file.toUri());
            if (!resource.exists()){
                throw  new ResponseStatusException(HttpStatus.NOT_FOUND, "Imahe introuvable");
            }
            return resource;

        } catch (MalformedURLException  e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Image introuvable");
        }
    }

    public void delete(String fileName){
        if (fileName == null) return;
        try {
            Path file = uploadDir.resolve(fileName).normalize();
            if (file.startsWith(uploadDir)){
                Files.deleteIfExists(file);
            }

        }catch (IOException ignored){
            //on ne bloque pas un utilisateur, pour un fichier qu'on pas pu nettoyer
        }
    }
}
