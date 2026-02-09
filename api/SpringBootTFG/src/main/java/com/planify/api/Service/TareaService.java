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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

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
        Calendario cal = calendarioRepository.findById(request.getIdCal()).orElseThrow(() -> new ResponseStatusException(NOT_FOUND,"Calendario no encontrado"));
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
            throw new ResponseStatusException(CONFLICT,"La tarea ya existe");
        }
        return new TareaNuevaResponseDTO(tarea.getId(),tarea.getNombre(),tarea.getTipo(),tarea.getEstado(),tarea.getFechaLim(),tarea.getColor());
    }
    @Transactional
    public TareaNuevaResponseDTO modificarTarea (TareaNuevaRequestDTO request, Integer idTarea){
        Calendario cal = calendarioRepository.findById(request.getIdCal()).orElseThrow(() ->  new ResponseStatusException(NOT_FOUND,"Calendario no encontrado"));
        Tarea tarea = tareaRepository.findById(idTarea).orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Tarea no existe"));
        tarea.setNombre(request.getNombre());
        tarea.setTipo(request.getTipo());
        tarea.setEstado(request.getEstado());
        tarea.setFechaLim(request.getFechaLim());
        tarea.setColor(request.getColor());
        tareaRepository.save(tarea);

        return new TareaNuevaResponseDTO(tarea.getId(),tarea.getNombre(),tarea.getTipo(),tarea.getEstado(),tarea.getFechaLim(),tarea.getColor());
    }
    @Transactional
    public List<TareaNuevaResponseDTO> obtenerTareasDia (Integer idCal, LocalDate fecha){
        LocalDateTime inicio = fecha.atStartOfDay();
        LocalDateTime fin = fecha.plusDays(1).atStartOfDay();

        List<Tarea> tareas = tareaRepository
                .findByCalendario_IdAndFechaLimBetweenOrderByFechaLimAsc(
                        idCal, inicio, fin
                );

        return tareas.stream()
                .map(t -> new TareaNuevaResponseDTO(
                        t.getId(),
                        t.getNombre(),
                        t.getTipo(),
                        t.getEstado(),
                        t.getFechaLim(),
                        t.getColor()
                ))
                .toList();
    }
    @Transactional
    public void eliminarTarea (Integer idTarea){
        tareaRepository.deleteById(idTarea);
    }
    @Transactional
    public List<TareaNuevaResponseDTO> obtenerTareas(Integer idCal){
        List<Tarea> tareas = tareaRepository.findByCalendario_IdOrderByFechaLimAsc(idCal);
        return tareas.stream()
                .map(t -> new TareaNuevaResponseDTO(
                        t.getId(),
                        t.getNombre(),
                        t.getTipo(),
                        t.getEstado(),
                        t.getFechaLim(),
                        t.getColor()
                )).toList();
    }
}
