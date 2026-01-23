package com.planify.api.Controllers;

import com.planify.api.POJOs.Usuario;
import com.planify.api.Service.AuthService;
import com.planify.api.dto.LoginRequestDTO;
import com.planify.api.dto.LoginResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
/*El requestMapping es para que el controlador se encuentre en la ruta /auth
 (osea que todo empieza por /auth/....)
  No maneja logica, solo llama al service que se necesita en cada caso*/

public class AuthController {
    private final AuthService authService;
    public AuthController(AuthService authService) {
        this.authService = authService;
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO request){
        /*->El @RequestBody LoginRequestDTO request significa que
        * todo lo que venga desde android en formato JSON (las credenciales del usuario que queremos
        *  comprobar), se van a combertin en un objeto LoginRequestDTO
        * ->El ResponseEntity<?> El responseEntity es un objeto que devuelve el servicio, sirve
        * para controlar errores, sin el, si el metodo falla se devuelve el http 200 por defecto
        * que significa OK. Pero si queremos controlarlo, lo hacemos con el responseEntity(Pondremos el 401
        * que significa Unauthorized)
        * El interogante solo nos dice que puede devolver cualquier tipo de objeto
        *
        * ResponseEntity es la forma correcta de devolver respuestas HTTP controlando el código y el contenido.*/
        try{
            Usuario usuario = authService.login(request);
            /*Convertimos el usuaro que acabamos de crear en un dto segura para mandarlo a Android
            * sin paswordhas y sin relaciones*/
            LoginResponseDTO response = new LoginResponseDTO(
                    usuario.getId(),
                    usuario.getNombre(),
                    usuario.getUsername(),
                    usuario.getEmail(),
                    usuario.getTelefono()
            );
            return ResponseEntity.ok(response);
            /*Si todo va bien significa 200(ok))*/
        }catch (RuntimeException e){
            return ResponseEntity.status(401).body("Credenciales incorrectas");
            /*Mandas el 401(Unauthorized)*/
        }

    }
}
