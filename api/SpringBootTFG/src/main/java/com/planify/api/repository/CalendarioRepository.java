package com.planify.api.repository;

import com.planify.api.POJOs.Calendario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CalendarioRepository extends JpaRepository<Calendario, Integer> {
    Optional<Calendario> findByCodigo(Integer codigo);
}
