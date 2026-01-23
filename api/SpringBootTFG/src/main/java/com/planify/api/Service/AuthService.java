package com.planify.api.Service;

import com.planify.api.POJOs.Usuario;
import com.planify.api.dto.LoginRequestDTO;
import com.planify.api.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    /*Este servicio(se llama asi porque es un servicio de autenticación)
    * se encarga de validar las credenciales del usuario que se quiere loguear
    * decide quien va a entrar y quien no
    * controla el acceso
    * NO ES UNA GESTION DE USUARIOS, SINO UN PROCESO DE AUTENTICACIÓN
    * No hace falta crear un AuthRepository pq usaremos los datos del usuario(UsuarioRepository)*/
    private final UsuarioRepository usuarioRepository;
    public AuthService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }
    public Usuario login(LoginRequestDTO request){
        /*Buscamos el usuario en la bd(con el findByEmail) con el email que nos llega por parametro(request.getEmail())*/
        Usuario usuario = usuarioRepository.findByEmail(request.getEmail()).orElseThrow(() -> new RuntimeException("Credenciales incorrectas"));

        /*Comparamos la contraseña del usuario que acabamos de encontrar con la del request
         que nos llega por parametro*/

        if(!usuario.getPasswordHash().equals(request.getPassword())){
            throw new RuntimeException("Credenciales incorrectas");
        }
        return usuario;
    }

}
