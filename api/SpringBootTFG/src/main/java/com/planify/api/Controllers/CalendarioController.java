package com.planify.api.Controllers;

import com.planify.api.Service.CalendarioService;
import com.planify.api.Service.ReluserCalService;
import com.planify.api.dto.CalendarioSimpleDTO;
import com.planify.api.dto.CrearCalendarioRequestDTO;
import com.planify.api.dto.UsuarioCalendarioDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/calendario")
public class CalendarioController {
    private final ReluserCalService relUserCalService;
    private final CalendarioService calendarioService;
    public CalendarioController(ReluserCalService relUserCalService, CalendarioService calendarioService) {
        this.relUserCalService = relUserCalService;
        this.calendarioService = calendarioService;
    }
    @GetMapping("/mios/{idUser}")
    public ResponseEntity<List<CalendarioSimpleDTO>> obtenerMisCalendarios (@PathVariable Integer idUser){
        return ResponseEntity.ok(relUserCalService.obtenerRelacionesDeUsuario(idUser));
    }
    @PostMapping("/crear/{idUser}")
    public ResponseEntity<CalendarioSimpleDTO> crearCalendario (@RequestBody@Valid CrearCalendarioRequestDTO request, @PathVariable Integer idUser){
        return ResponseEntity.ok(calendarioService.crearCalendario(request,idUser));
    }
    @DeleteMapping("/eliminar/{idCal}/{idUser}")
    public ResponseEntity<Void> eliminarCalendario (@PathVariable Integer idCal,@PathVariable Integer idUser){
        calendarioService.salirOeliminarCalendario(idCal, idUser);
        return ResponseEntity.noContent().build();
    }
    @PostMapping("/unirse/{Codigo}/{idUser}")
    public ResponseEntity<CalendarioSimpleDTO> unirseCalendario(@PathVariable String Codigo, @PathVariable Integer idUser){
        return ResponseEntity.ok(calendarioService.unirseCalendario(Codigo, idUser));
    }
    @GetMapping("/verMiembros/{idCal}")
    public ResponseEntity<List<UsuarioCalendarioDTO>> obtenerMiembros(@PathVariable Integer idCal){
        return ResponseEntity.ok(relUserCalService.obtenerMiembros(idCal));
    }


}
