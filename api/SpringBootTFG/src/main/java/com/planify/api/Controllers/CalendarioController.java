package com.planify.api.Controllers;

import com.planify.api.POJOs.RelUserCal;
import com.planify.api.Service.CalendarioService;
import com.planify.api.Service.ReluserCalService;
import com.planify.api.dto.CalendarioSimpleDTO;
import com.planify.api.dto.CrearCalendarioRequestDTO;
import com.planify.api.repository.RelUserCalRepository;
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


}
