package com.planify.api.Controllers;

import com.planify.api.POJOs.Tarea;
import com.planify.api.Service.TareaService;
import com.planify.api.Service.UsuarioService;
import com.planify.api.dto.TareaNuevaRequestDTO;
import com.planify.api.dto.TareaNuevaResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tarea")
public class TareaController {

    private final TareaService tareaService;

    public TareaController(TareaService tareaService) {
        this.tareaService = tareaService;
    }
    @PostMapping("/crearTarea")
    public ResponseEntity<TareaNuevaResponseDTO> crearTarea(@RequestBody TareaNuevaRequestDTO request){
        return ResponseEntity.ok(tareaService.crearTarea(request));
    }
    /*@GetMapping("/verTarea")
    public ResponseEntity<List<TareaNuevaResponseDTO>> verTarea(){
    }*/

}
