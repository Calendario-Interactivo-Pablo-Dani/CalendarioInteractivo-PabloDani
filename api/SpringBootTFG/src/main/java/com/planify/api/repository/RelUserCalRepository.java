package com.planify.api.repository;

import com.planify.api.POJOs.RelUserCal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RelUserCalRepository extends JpaRepository<RelUserCal, Integer> {
    /* Devuelve todas las relaciones usuario-calendario de un usuario concreto
    *Dentro de las relaciones vamos a poder sacar:
    *-Calendario
    *- rol
    * -id de la relación
    * */
    List<RelUserCal> findByIdUser_Id(Integer idUser);
    /*Devuelve la relacion de un usuario y un calendario en especifco
    * Esto nos va a servir pq al tener la relacion guardada en un pojo
    * simplemente con un .delete(relacion) vamos a ser capaces de eliminarla*/
    Optional<RelUserCal> findByIdUser_IdAndIdCal_Id(Integer idUser, Integer idCal);
    //Elimina las relaciones de un calendario
    void deleteByIdCal_Id(Integer idCal);
    List<RelUserCal> findByIdCal_Id(Integer idCal);
    int countByIdUser_Id(Integer idUser);

}
