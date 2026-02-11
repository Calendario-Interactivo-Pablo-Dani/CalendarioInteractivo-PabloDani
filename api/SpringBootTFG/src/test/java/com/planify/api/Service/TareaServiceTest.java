package com.planify.api.Service;

import com.planify.api.POJOs.*;
import com.planify.api.dto.*;
import com.planify.api.enums.*;
import com.planify.api.repository.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TareaServiceTest {

    @Mock
    private TareaRepository tareaRepository;

    @Mock
    private CalendarioRepository calendarioRepository;

    @InjectMocks
    private TareaService tareaService;

    //CREAR TAREA
    @Test
    void crearTarea_correcto() {
        TareaNuevaRequestDTO request = new TareaNuevaRequestDTO();
        request.setIdCal(1);
        request.setNombre("Examen DAM");
        request.setTipo(TipoTarea.tarea);
        request.setEstado(EstadoTarea.pendiente);
        request.setFechaLim(LocalDateTime.now());
        request.setColor(ColorTarea.ROJO);

        Calendario calendario = new Calendario();
        calendario.setId(1);

        Tarea tarea = new Tarea();
        tarea.setId(10);
        tarea.setNombre("Examen DAM");
        tarea.setTipo(TipoTarea.tarea);
        tarea.setEstado(EstadoTarea.pendiente);
        tarea.setFechaLim(request.getFechaLim());
        tarea.setColor(ColorTarea.ROJO);

        when(calendarioRepository.findById(1))
                .thenReturn(Optional.of(calendario));

        when(tareaRepository.save(any(Tarea.class)))
                .thenReturn(tarea);

        TareaNuevaResponseDTO dto =
                tareaService.crearTarea(request);

        assertNotNull(dto);
        assertEquals(10, dto.getIdTarea());
        assertEquals("Examen DAM", dto.getNombre());
        assertEquals(ColorTarea.ROJO, dto.getColor());
    }

    @Test
    void crearTarea_calendarioNoExiste() {
        TareaNuevaRequestDTO request = new TareaNuevaRequestDTO();
        request.setIdCal(99);

        when(calendarioRepository.findById(99))
                .thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> tareaService.crearTarea(request)
        );

        assertEquals(404, ex.getStatusCode().value());
        verify(tareaRepository, never()).save(any());
    }

    @Test
    void crearTarea_duplicada() {
        TareaNuevaRequestDTO request = new TareaNuevaRequestDTO();
        request.setIdCal(1);

        Calendario calendario = new Calendario();
        calendario.setId(1);

        when(calendarioRepository.findById(1))
                .thenReturn(Optional.of(calendario));

        when(tareaRepository.save(any(Tarea.class)))
                .thenThrow(DataIntegrityViolationException.class);

        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> tareaService.crearTarea(request)
        );

        assertEquals(409, ex.getStatusCode().value());
    }

    //MODIFICAR TAREA
    @Test
    void modificarTarea_correcto() {
        TareaNuevaRequestDTO request = new TareaNuevaRequestDTO();
        request.setIdCal(1);
        request.setNombre("Entrega TFG");
        request.setTipo(TipoTarea.tarea);
        request.setEstado(EstadoTarea.finalizada);
        request.setFechaLim(LocalDateTime.now());
        request.setColor(ColorTarea.VERDE);

        Calendario calendario = new Calendario();
        calendario.setId(1);

        Tarea tarea = new Tarea();
        tarea.setId(5);
        tarea.setCalendario(calendario);

        when(calendarioRepository.findById(1))
                .thenReturn(Optional.of(calendario));

        when(tareaRepository.findById(5))
                .thenReturn(Optional.of(tarea));

        TareaNuevaResponseDTO dto =
                tareaService.modificarTarea(request, 5);

        assertEquals("Entrega TFG", dto.getNombre());
        assertEquals(EstadoTarea.finalizada, dto.getEstado());
    }

    @Test
    void modificarTarea_noPerteneceCalendario() {
        TareaNuevaRequestDTO request = new TareaNuevaRequestDTO();
        request.setIdCal(1);

        Calendario otroCalendario = new Calendario();
        otroCalendario.setId(2);

        Tarea tarea = new Tarea();
        tarea.setCalendario(otroCalendario);

        when(calendarioRepository.findById(1))
                .thenReturn(Optional.of(new Calendario()));

        when(tareaRepository.findById(5))
                .thenReturn(Optional.of(tarea));

        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> tareaService.modificarTarea(request, 5)
        );

        assertEquals(403, ex.getStatusCode().value());
    }

    //OBTENER TAREAS DEL DIA
    @Test
    void obtenerTareasDia_correcto() {
        LocalDate fecha = LocalDate.of(2026, 2, 10);

        Tarea tarea = new Tarea();
        tarea.setId(1);
        tarea.setNombre("Evento");
        tarea.setFechaLim(fecha.atTime(10, 0));
        tarea.setColor(ColorTarea.AZUL);

        when(tareaRepository
                .findByCalendario_IdAndFechaLimBetweenOrderByFechaLimAsc(
                        eq(1), any(), any()))
                .thenReturn(List.of(tarea));

        List<TareaNuevaResponseDTO> resultado =
                tareaService.obtenerTareasDia(1, fecha);

        assertEquals(1, resultado.size());
        assertEquals("Evento", resultado.get(0).getNombre());
    }

    //EVENTOS MES
    @Test
    void eventosMes_agrupaCorrectamente() {
        LocalDateTime fecha = LocalDate.of(2026, 2, 10).atTime(10, 0);

        Tarea t1 = new Tarea();
        t1.setFechaLim(fecha);
        t1.setColor(ColorTarea.ROJO);

        Tarea t2 = new Tarea();
        t2.setFechaLim(fecha);
        t2.setColor(ColorTarea.AZUL);

        when(tareaRepository.findByCalendario_IdAndFechaLimBetween(
                eq(1), any(), any()))
                .thenReturn(List.of(t1, t2));

        List<DiasConTareaDTO> resultado =
                tareaService.eventosMes(1, 2026, 2);

        assertEquals(1, resultado.size());
        assertEquals(2, resultado.get(0).getColores().size());
    }
    //ELIMINAR TAREA
    @Test
    void eliminarTarea_correcto() {
        tareaService.eliminarTarea(5);
        verify(tareaRepository).deleteById(5);
    }




}