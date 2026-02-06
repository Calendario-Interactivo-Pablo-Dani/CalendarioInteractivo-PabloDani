package com.planify.api.Service;

import com.planify.api.POJOs.Calendario;
import com.planify.api.POJOs.RelUserCal;
import com.planify.api.dto.CalendarioSimpleDTO;
import com.planify.api.dto.UsuarioCalendarioDTO;
import com.planify.api.repository.CalendarioRepository;
import com.planify.api.repository.RelUserCalRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class ReluserCalService {
    private RelUserCalRepository relUserCalRepository;
    private CalendarioRepository calendarioRepository;

    public ReluserCalService(RelUserCalRepository relUserCalRepository, CalendarioRepository calendarioRepository) {
        this.relUserCalRepository = relUserCalRepository;
        this.calendarioRepository = calendarioRepository;
    }
    //Devuelve todas las relaciones usuario-calendario de un usuario
    @Transactional
    public List<CalendarioSimpleDTO> obtenerRelacionesDeUsuario(Integer idUser){
        List<RelUserCal> relaciones = relUserCalRepository.findByIdUser_Id(idUser);

        List<CalendarioSimpleDTO> resultado = new ArrayList<>();

        for (RelUserCal r : relaciones) {
            CalendarioSimpleDTO dto = new CalendarioSimpleDTO(
                    r.getIdCal().getId(),
                    r.getIdCal().getNombre(),
                    r.getIdCal().getCodigo(),
                    r.getRol()
            );

            resultado.add(dto);
        }

        return resultado;
    }
    //Devuelve todas las relaciones usuario-calendario de un calendario
    @Transactional
    public List<UsuarioCalendarioDTO> obtenerMiembros(Integer idCal){

        List<UsuarioCalendarioDTO> resultado = new ArrayList<>();

        List<RelUserCal> relaciones = relUserCalRepository.findByIdCal_Id(idCal);

        for(RelUserCal r : relaciones){
            UsuarioCalendarioDTO dto = new UsuarioCalendarioDTO(
                    r.getIdUser().getId(),
                    r.getIdUser().getNombre(),
                    r.getIdUser().getEmail(),
                    r.getRol().toString()
            );
            resultado.add(dto);
        }
        return resultado;
    }
    @Transactional
    public int obtenerTotalCalendarios(Integer idUser){
        return relUserCalRepository.countByIdUser_Id(idUser);
    }

}
