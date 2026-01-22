package com.planify.api.repository;

import com.planify.api.POJOs.Calendario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CalendarioRepository extends JpaRepository<Calendario, Integer> {
}
