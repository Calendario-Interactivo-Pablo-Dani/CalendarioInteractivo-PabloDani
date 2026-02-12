package com.planify.api.repository;

import com.planify.api.POJOs.Tarea;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TareaRepository extends JpaRepository<Tarea, Integer> {
    List<Tarea> findByCalendario_IdAndFechaLimBetweenOrderByFechaLimAsc(Integer idCal, LocalDateTime inicio, LocalDateTime fin);
    List<Tarea> findByCalendario_IdOrderByFechaLimAsc(Integer idCal);
    List<Tarea> findByCalendario_IdAndFechaLimBetween(Integer idCal, LocalDateTime inicio, LocalDateTime fin);
    void deleteByCalendario_Id(Integer idCal);

}
