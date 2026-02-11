package com.planify.api.Service;

import com.planify.api.POJOs.Calendario;
import com.planify.api.POJOs.Tarea;
import com.planify.api.dto.DiasConTareaDTO;
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
import java.util.*;

import static org.springframework.http.HttpStatus.*;

@Service
public class TareaService {

    private final TareaRepository tareaRepository;
    private final CalendarioRepository calendarioRepository;
    public TareaService(TareaRepository tareaRepository, CalendarioRepository calendarioRepository) {
        this.tareaRepository = tareaRepository;
        this.calendarioRepository = calendarioRepository;
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
        Tarea tareaGuardada;
        try{
            /*Insertamos la tarea en la BD*/
             tareaGuardada = tareaRepository.save(tarea);
        }catch(DataIntegrityViolationException e){
            throw new ResponseStatusException(CONFLICT,"La tarea ya existe");
        }
        return new TareaNuevaResponseDTO(tareaGuardada.getId(),tareaGuardada.getNombre(),tareaGuardada.getTipo(),tareaGuardada.getEstado(),tareaGuardada.getFechaLim(),tareaGuardada.getColor());
    }
    @Transactional
    public TareaNuevaResponseDTO modificarTarea (TareaNuevaRequestDTO request, Integer idTarea){
        Calendario cal = calendarioRepository.findById(request.getIdCal()).orElseThrow(() ->  new ResponseStatusException(NOT_FOUND,"Calendario no encontrado"));
        Tarea tarea = tareaRepository.findById(idTarea).orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Tarea no existe"));
        if (!tarea.getCalendario().getId().equals(request.getIdCal())) {
            throw new ResponseStatusException(
                    FORBIDDEN,
                    "La tarea no pertenece a este calendario"
            );
        }
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
    @Transactional
    public List<DiasConTareaDTO> eventosMes(Integer idCal, Integer anio, Integer mes){
        /*CALCULAMOS EL RANGO DE FECHAS DEL MES*/
        LocalDate inicioMes = LocalDate.of(anio, mes, 1);
        LocalDate inicioMesSiguiente = inicioMes.plusMonths(1);

        LocalDateTime inicio = inicioMes.atStartOfDay();
        LocalDateTime fin = inicioMesSiguiente.atStartOfDay();
        List<Tarea> tareasDelMes = tareaRepository.findByCalendario_IdAndFechaLimBetween(idCal, inicio, fin);

        /*AGRUPAR LAS TARES POR DIA Y QUEDARONS CON LOS COLORES*/
        Map<LocalDate, Set<String>> mapa = new HashMap<>();
        for(Tarea t : tareasDelMes){
            LocalDate dia = t.getFechaLim().toLocalDate();
            String color = t.getColor().toString();

            mapa.computeIfAbsent(dia, k -> new HashSet<>()).add(color);
        }
        /*CONVERTIR EL MAPA A LISTA DE LOS DTOS QUE VAMOS A DEVOLVER*/
        List<DiasConTareaDTO> resultado = new ArrayList<>();

        for (Map.Entry<LocalDate, Set<String>> entry : mapa.entrySet()) {
            LocalDate fecha = entry.getKey();
            List<String> colores = new ArrayList<>(entry.getValue());

            resultado.add(new DiasConTareaDTO(fecha, colores));
        }

        return resultado;


    }
}
