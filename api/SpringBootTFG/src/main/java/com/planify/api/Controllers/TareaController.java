package com.planify.api.Controllers;

import com.planify.api.POJOs.Tarea;
import com.planify.api.Service.TareaService;
import com.planify.api.Service.UsuarioService;
import com.planify.api.dto.TareaNuevaRequestDTO;
import com.planify.api.dto.TareaNuevaResponseDTO;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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
    @PostMapping("/modificarTarea/{idTarea}")
    public ResponseEntity<TareaNuevaResponseDTO> modificarTarea(@RequestBody TareaNuevaRequestDTO request, @PathVariable Integer idTarea){
        return ResponseEntity.ok(tareaService.modificarTarea(request, idTarea));
    }
    @GetMapping("/verTareasDia/{idCal}/{fecha}")
    public ResponseEntity<List<TareaNuevaResponseDTO>> obtenerTareasDia(@PathVariable Integer idCal, @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha){
        return ResponseEntity.ok(tareaService.obtenerTareasDia(idCal,fecha));
    }
    @GetMapping("/verTareas/{idCal}")
    public ResponseEntity<List<TareaNuevaResponseDTO>> obtenerTareas(@PathVariable Integer idCal){
        return ResponseEntity.ok(tareaService.obtenerTareas(idCal));
    }
    @DeleteMapping("/eliminarTarea/{idTarea}")
    public ResponseEntity<Void> eliminarTarea(@PathVariable Integer idTarea){
        tareaService.eliminarTarea(idTarea);
        return ResponseEntity.noContent().build();
    }

}
