package com.planify.api.repository;

import com.planify.api.POJOs.Tarea;
import org.springframework.data.jpa.repository.JpaRepository;

interface TareaRepository extends JpaRepository<Tarea, Integer> {
}
