package com.planify.api.Service;

import com.planify.api.POJOs.Calendario;
import com.planify.api.POJOs.RelUserCal;
import com.planify.api.POJOs.Usuario;
import com.planify.api.dto.CalendarioSimpleDTO;
import com.planify.api.dto.CrearCalendarioRequestDTO;
import com.planify.api.enums.RolUsuarioCalendario;
import com.planify.api.repository.CalendarioRepository;
import com.planify.api.repository.RelUserCalRepository;
import com.planify.api.repository.UsuarioRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.*;


@Service
public class CalendarioService {
 private final CalendarioRepository calendarioRepository;
 private final RelUserCalRepository relUserCalRepository;
 private final UsuarioRepository usuarioRepository;
 public CalendarioService(CalendarioRepository calendarioRepository, RelUserCalRepository relUserCalRepository, UsuarioRepository usuarioRepository) {
     this.calendarioRepository = calendarioRepository;
     this.relUserCalRepository = relUserCalRepository;
     this.usuarioRepository = usuarioRepository;
 }
 /*Como en esta operacion vamos a usar 2 insert diferentes, para evitar errores del tipo
 * 1 paso funciona y otro no(un calendario se guarda pero su relación no)
 * o se hacen los dos insert bien o no se hace ninguno*/
 @Transactional
 /*Le pasamos el calendario y el usuario*/
 public CalendarioSimpleDTO crearCalendario(CrearCalendarioRequestDTO request, Integer idUser){
     /*Buscamos el usuario con el id que nos llega por parametro(idUser)*/
     Usuario usuario = usuarioRepository.findById(idUser).orElseThrow(() -> new ResponseStatusException(NOT_FOUND,"Usuario no existe"));
     /*Creamos el calendario*/
     Calendario cal = new Calendario();
     cal.setNombre(request.getNombre());
     cal.setCodigo(request.getCodigo());

     try{
         /*Insertamos el calendario en la bd*/
         cal = calendarioRepository.save(cal);
     }catch(DataIntegrityViolationException e){
         throw new ResponseStatusException(CONFLICT,"Calendario ya existe");
     }
     /*Creamos la relación entre el usuario y el calendario*/
     RelUserCal rel = new RelUserCal();
     rel.setIdCal(cal);
     rel.setIdUser(usuario);
     rel.setRol(RolUsuarioCalendario.owner);
     /*Guardamos la relación en la bd*/
     relUserCalRepository.save(rel);
     /*Devolvemos el calendario*/
     return new CalendarioSimpleDTO(cal.getId(),cal.getNombre(),cal.getCodigo(),RolUsuarioCalendario.owner);

 }

 @Transactional
 public void salirOeliminarCalendario(Integer idCal, Integer idUser){
     //Comprobamos que el calendario existe
     Calendario calendario = calendarioRepository.findById(idCal).orElseThrow(() -> new ResponseStatusException(NOT_FOUND,"Calendario no existe"));
     //Comprobamos que el usuario pertenece al calendario
     //Ponemos FORBIDDEN y no NOT_FOUND pq el calendario si existe, pero el usuario no tiene permiso
     RelUserCal relacion = relUserCalRepository.findByIdUser_IdAndIdCal_Id(idUser,idCal).orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN,"Usuario no pertenece al calendario"));
     if(relacion.getRol() == RolUsuarioCalendario.owner){
         //CASO OWNER
         //Borramos las relaciones del calendario
         relUserCalRepository.deleteByIdCal_Id(idCal);
         //Borramos el calendario
         calendarioRepository.delete(calendario);

     }else{
         //CASO MIEMBRO
         //Salimos del calendario
         relUserCalRepository.delete(relacion);

     }

 }
}
