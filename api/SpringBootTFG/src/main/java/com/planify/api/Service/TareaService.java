package com.planify.api.Service;

import com.planify.api.POJOs.Tarea;
import com.planify.api.repository.TareaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TareaService {

    private final TareaRepository tareaRepository;
    public TareaService(TareaRepository tareaRepository) {
        this.tareaRepository = tareaRepository;
    }

    public List<Tarea> findAll() {
        return tareaRepository.findAll();
    }

    public Tarea findById(Integer id) {
        return tareaRepository.findById(id).orElseThrow(() -> new RuntimeException("Tarea no encontrado"));
    }
}
