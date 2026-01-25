package com.planify.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/*Le indicamos a Spring que es un controlador de excepciones
* y se encarga de manejarlas de cualquier controller
* asi cuando se lance una excepción se recibe aquí*/
@ControllerAdvice
public class GlobalExceptionHandler {
    /*Errores de validación(@Email,@Size, @NotBlank...)
    * CUANDO FALLA ALGO ANTES DE ENTRAR AL CONTROLLER(dto)
    * VA A MANEJAR LOS ERRORES DEL REGISTRO
     * pq solo los que pueden dar los fallos que queremos manejar*/
    @ExceptionHandler(MethodArgumentNotValidException.class)
    /*Significa que cuando ocurra esta excepción se use el método de abajo
    * Esta excepción se lanza cuando usas el @Valid
    * en un dto y este NO CUMPLE LAS REGLAS QUE LE HEMOS PUESTO
    * */
    public ResponseEntity<?> handleValidationError(MethodArgumentNotValidException ex){

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });
        /*error.getField() es el nombre del campo que no cumple con las reglas(email, password...)
        * error.getDefaultMessage() es el mensaje de error que pusimos en la anotación del dto*/
        Map<String, Object> response = new HashMap<>();
        response.put("errors", errors);
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("timestamp", LocalDateTime.now());

        return ResponseEntity.badRequest().body(response);
    }
    /*Errores de lógica (email duplicado, username duplicado...)*/
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleValidationError(RuntimeException ex){
        Map<String, Object> response = new HashMap<>();
        response.put("message", ex.getMessage());
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("timestamp", LocalDateTime.now());

        return ResponseEntity.badRequest().body(response);
    }
}
