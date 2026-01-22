package com.planify.api.Service;

import com.planify.api.POJOs.Usuario;
import com.planify.api.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/*Le indicamos a Spring que es un servicio*/
@Service
public class UsuarioService {
    /*Aquí es donde se maneja la lógica y donde se toman las decisiones
     * Para eso usamos el repositorio que hemos creado anteriormente, asi podemos
     * usar los datos de la base de datos*/
    private final UsuarioRepository usuarioRepository;
    /*Constructor al que le pasamos el repositorio*/
    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }
    /**/
    public List<Usuario> findAll(){
        return usuarioRepository.findAll();
    }
    public Usuario findById(Integer id){
        return usuarioRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

}
