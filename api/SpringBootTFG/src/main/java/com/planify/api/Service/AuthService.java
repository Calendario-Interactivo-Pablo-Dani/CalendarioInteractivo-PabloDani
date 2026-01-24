package com.planify.api.Service;

import com.planify.api.POJOs.Usuario;
import com.planify.api.dto.LoginRequestDTO;
import com.planify.api.dto.RegisterRequestDTO;
import com.planify.api.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final PasswordEncoder passwordEncoder;
    public AuthService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }
    public Usuario login(LoginRequestDTO request){
        /*Buscamos el usuario en la bd(con el findByEmail) con el email que nos llega por parametro(request.getEmail())*/
        Usuario usuario = usuarioRepository.findByEmail(request.getEmail()).orElseThrow(() -> new RuntimeException("Credenciales incorrectas"));

        /*Comparamos la contraseña del usuario que acabamos de encontrar con la del request
         que nos llega por parametro
         El passwordEncoder compara las 2 contraseñas ya encriptadas*/
        if(!passwordEncoder.matches(request.getPassword(), usuario.getPasswordHash())){
            throw new RuntimeException("Credenciales incorrectas");
        }
        return usuario;
    }

    public Usuario register(RegisterRequestDTO request){
        /*Comprobamos que el email no sea uno que ya se esté utilizando*/
        if (usuarioRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("El email ya está registrado");
        }

        // Comprobar username no se este utilizando
        if (usuarioRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("El username ya está en uso");
        }

        Usuario usuario = new Usuario();

        usuario.setEmail(request.getEmail());
        usuario.setTelefono(request.getTelefono());
        usuario.setNombre(request.getNombre());
        usuario.setUsername(request.getUsername());
        /*La contraseña del request viene en formato normal, asi no la podemos guardar en la bd
        * Asi que la encriptamos con el passwordEncoder*/
        usuario.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        /*Lo guardamos en la bd y lo devuelve*/
        return usuarioRepository.save(usuario);
    }

}
