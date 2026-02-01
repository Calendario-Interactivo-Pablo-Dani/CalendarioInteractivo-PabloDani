package com.planify.api.repository;

import com.planify.api.POJOs.RelUserCal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RelUserCalRepository extends JpaRepository<RelUserCal, Integer> {
    /* Devuelve todas las relaciones usuario-calendario de un usuario concreto
    *Dentro de las relaciones vamos a poder sacar:
    *-Calendario
    *- rol
    * -id de la relación
    * */
    List<RelUserCal> findByIdUser_Id(Integer idUser);
}
