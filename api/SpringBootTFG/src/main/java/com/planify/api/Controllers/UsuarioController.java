package com.planify.api.Controllers;

import com.planify.api.POJOs.Usuario;
import com.planify.api.Service.ReluserCalService;
import com.planify.api.Service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final ReluserCalService reluserCalService;

    public UsuarioController(UsuarioService usuarioService, ReluserCalService reluserCalService) {
        this.usuarioService = usuarioService;
        this.reluserCalService = reluserCalService;
    }
    @GetMapping
    public List<Usuario> obtenerTodos(){
        return usuarioService.findAll();
    }
    @GetMapping("/{id}")
    public Usuario obtenerPorId(Integer id){
        return usuarioService.findById(id);
    }
    @GetMapping("/totalCalendarios/{idUser}")
    public ResponseEntity<Integer> obtenerTotalCalendarios(@PathVariable Integer idUser){
        return ResponseEntity.ok(reluserCalService.obtenerTotalCalendarios(idUser));
    }

}
