package com.planify.api.Controllers;

import com.planify.api.POJOs.Usuario;
import com.planify.api.Service.UsuarioService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }
    @GetMapping
    public List<Usuario> obtenerTodos(){
        return usuarioService.findAll();
    }
    @GetMapping("/{id}")
    public Usuario obtenerPorId(Integer id){
        return usuarioService.findById(id);
    }

}
