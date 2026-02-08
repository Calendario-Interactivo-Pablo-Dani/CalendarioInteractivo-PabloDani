package com.planify.api.Service;

import com.planify.api.POJOs.Calendario;
import com.planify.api.POJOs.Tarea;
import com.planify.api.dto.TareaNuevaRequestDTO;
import com.planify.api.dto.TareaNuevaResponseDTO;
import com.planify.api.repository.CalendarioRepository;
import com.planify.api.repository.TareaRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.springframework.http.HttpStatus.CONFLICT;

@Service
public class TareaService {

    private final TareaRepository tareaRepository;
    private final CalendarioRepository calendarioRepository;
    public TareaService(TareaRepository tareaRepository, CalendarioRepository calendarioRepository) {
        this.tareaRepository = tareaRepository;
        this.calendarioRepository = calendarioRepository;
    }

    public List<Tarea> findAll() {
        return tareaRepository.findAll();
    }

    public Tarea findById(Integer id) {
        return tareaRepository.findById(id).orElseThrow(() -> new RuntimeException("Tarea no encontrado"));
    }
    @Transactional
    public TareaNuevaResponseDTO crearTarea (TareaNuevaRequestDTO request) {
        Calendario cal = calendarioRepository.findById(request.getIdCal()).orElseThrow(() -> new RuntimeException("Calendario no encontrado"));
        Tarea tarea = new Tarea();
        tarea.setNombre(request.getNombre());
        tarea.setCalendario(cal);
        tarea.setTipo(request.getTipo());
        tarea.setEstado(request.getEstado());
        tarea.setFechaLim(request.getFechaLim());
        tarea.setColor(request.getColor());
        try{
            /*Insertamos la tarea en la BD*/
             tareaRepository.save(tarea);
        }catch(DataIntegrityViolationException e){
            throw new ResponseStatusException(CONFLICT,"Calendario ya existe");
        }
        return new TareaNuevaResponseDTO(tarea.getNombre(),tarea.getTipo(),tarea.getEstado(),tarea.getFechaLim(),tarea.getColor());
    }
}
