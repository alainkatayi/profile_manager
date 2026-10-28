package com.monapp.profilapp.exception;

import com.monapp.profilapp.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


@RestControllerAdvice //il surveille tous les controllers
public class GlobalExceptionHandler {

    //cas 1: les erreurs qu'on lance nous memes dans les services
    @ExceptionHandler(ResponseStatusException.class)
    public  ResponseEntity<ErrorResponse> handleResponseStatus(ResponseStatusException ex){
        HttpStatus status = HttpStatus.valueOf(ex.getStatusCode().value());

        ErrorResponse body =  new ErrorResponse(status.value(), ex.getReason(), null, LocalDateTime.now());

        return ResponseEntity.status(status).body(body);
    }

    //cas 2 validation echoué(@valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex){
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().
                forEach(error -> errors.put(error.getField(),
                        error.getDefaultMessage()));

        ErrorResponse body = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(), "Données invalides",
                errors,
                LocalDateTime.now()
        );
        return ResponseEntity.badRequest().body(body);
    }
}
