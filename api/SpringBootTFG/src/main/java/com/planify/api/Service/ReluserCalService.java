package com.planify.api.Service;

import com.planify.api.POJOs.RelUserCal;
import com.planify.api.dto.CalendarioSimpleDTO;
import com.planify.api.repository.RelUserCalRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReluserCalService {
    private RelUserCalRepository relUserCalRepository;
    public ReluserCalService(RelUserCalRepository relUserCalRepository) {
        this.relUserCalRepository = relUserCalRepository;
    }
    //Devuelve todas las relaciones usuario-calendario de un usuario
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

}
