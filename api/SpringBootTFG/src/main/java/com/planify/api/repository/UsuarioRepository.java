package com.planify.api.repository;

import com.planify.api.POJOs.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
/*El repositorio es una interfaz que nos permite acceder a la base de datos
 * y realizar consultas a la misma
 * JpaRepository es la interfaz que nos permite realizar consultas a la base de datos
 * solo consultas y persistencia(Guarda y busca) No toma decisiones*/

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    // EN LOS REPOSITORIES SOLO SE CREAN FIND/DELETES/UPDATES PARA PARAMETROS
    // QUE NO SON AUTOMATICOS(osea todos menos id y alguna excepción más)


    /*Este metodo genera una consulta para buscar un usuario por su email
     * equivale a SELECT * FROM usuario WHERE email = ?*/
    Optional<Usuario> findByEmail(String email);

    /*Lo mismo que el anterior pero por username*/
    Optional<Usuario>findByUsername(String username);

    /*El nombre del metodo debe ser el mismo que el del atributo del POJO*/

}
