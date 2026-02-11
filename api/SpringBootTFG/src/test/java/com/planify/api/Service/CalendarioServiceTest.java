package com.planify.api.Service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.planify.api.POJOs.*;
import com.planify.api.dto.*;
import com.planify.api.enums.RolUsuarioCalendario;
import com.planify.api.repository.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CalendarioServiceTest {
    @Mock
    private CalendarioRepository calendarioRepository;

    @Mock
    private RelUserCalRepository relUserCalRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private CalendarioService calendarioService;

    //CREAR CALENDARIO
    @Test
    void crearCalendario_correcto() {
        // Datos de entrada
        Integer idUser = 1;
        CrearCalendarioRequestDTO request = new CrearCalendarioRequestDTO();
        request.setNombre("Calendario DAM");

        Usuario usuario = new Usuario();
        usuario.setId(idUser);

        Calendario calendario = new Calendario();
        calendario.setId(10);
        calendario.setNombre("Calendario DAM");
        calendario.setCodigo("ABC123");

        // Comportamiento de los repositorios
        when(usuarioRepository.findById(idUser)).thenReturn(Optional.of(usuario));
        when(calendarioRepository.save(any(Calendario.class))).thenReturn(calendario);

        // Ejecución
        CalendarioSimpleDTO resultado = calendarioService.crearCalendario(request, idUser);

        // Verificaciones
        assertNotNull(resultado);
        assertEquals(10, resultado.getIdCal());
        assertEquals("Calendario DAM", resultado.getNombre());
        assertEquals("ABC123", resultado.getCodigo());
        assertEquals(RolUsuarioCalendario.owner, resultado.getRol());

        verify(calendarioRepository).save(any(Calendario.class));
        verify(relUserCalRepository).save(any(RelUserCal.class));
    }

    @Test
    void crearCalendario_usuarioNoExiste() {
        Integer idUser = 99;

        CrearCalendarioRequestDTO request = new CrearCalendarioRequestDTO();
        request.setNombre("Calendario DAM");

        when(usuarioRepository.findById(idUser))
                .thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> calendarioService.crearCalendario(request, idUser)
        );

        assertEquals(404, ex.getStatusCode().value());

        verify(calendarioRepository, never()).save(any());
        verify(relUserCalRepository, never()).save(any());
    }

    @Test
    void crearCalendario_calendarioDuplicado() {
        Integer idUser = 1;

        CrearCalendarioRequestDTO request = new CrearCalendarioRequestDTO();
        request.setNombre("Calendario DAM");

        Usuario usuario = new Usuario();
        usuario.setId(idUser);

        when(usuarioRepository.findById(idUser))
                .thenReturn(Optional.of(usuario));

        when(calendarioRepository.save(any(Calendario.class)))
                .thenThrow(DataIntegrityViolationException.class);

        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> calendarioService.crearCalendario(request, idUser)
        );

        assertEquals(409, ex.getStatusCode().value());

        verify(relUserCalRepository, never()).save(any());
    }

    //SALIR O ELIMINAR CALENDARIO
    @Test
    void salirOeliminarCalendario_ownerElimina() {
        Integer idCal = 1;
        Integer idUser = 1;

        Calendario calendario = new Calendario();
        calendario.setId(idCal);

        RelUserCal relacion = new RelUserCal();
        relacion.setRol(RolUsuarioCalendario.owner);

        when(calendarioRepository.findById(idCal))
                .thenReturn(Optional.of(calendario));

        when(relUserCalRepository.findByIdUser_IdAndIdCal_Id(idUser, idCal))
                .thenReturn(Optional.of(relacion));

        calendarioService.salirOeliminarCalendario(idCal, idUser);

        verify(relUserCalRepository).deleteByIdCal_Id(idCal);
        verify(calendarioRepository).delete(calendario);
        verify(relUserCalRepository, never()).delete(relacion);
    }

    @Test
    void salirOeliminarCalendario_miembroSale() {
        Integer idCal = 1;
        Integer idUser = 2;

        Calendario calendario = new Calendario();
        calendario.setId(idCal);

        RelUserCal relacion = new RelUserCal();
        relacion.setRol(RolUsuarioCalendario.member);

        when(calendarioRepository.findById(idCal))
                .thenReturn(Optional.of(calendario));

        when(relUserCalRepository.findByIdUser_IdAndIdCal_Id(idUser, idCal))
                .thenReturn(Optional.of(relacion));

        calendarioService.salirOeliminarCalendario(idCal, idUser);

        verify(relUserCalRepository).delete(relacion);
        verify(calendarioRepository, never()).delete(any());
        verify(relUserCalRepository, never()).deleteByIdCal_Id(any());
    }

    @Test
    void salirOeliminarCalendario_usuarioNoPertenece() {
        Integer idCal = 1;
        Integer idUser = 5;

        Calendario calendario = new Calendario();
        calendario.setId(idCal);

        when(calendarioRepository.findById(idCal))
                .thenReturn(Optional.of(calendario));

        when(relUserCalRepository.findByIdUser_IdAndIdCal_Id(idUser, idCal))
                .thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> calendarioService.salirOeliminarCalendario(idCal, idUser)
        );

        assertEquals(403, ex.getStatusCode().value());
    }

    //UNIRSE CALENDARIO
    @Test
    void unirseCalendario_correcto() {
        String codigo = "ABC123";
        Integer idUser = 1;

        Calendario calendario = new Calendario();
        calendario.setId(5);
        calendario.setNombre("Calendario Compartido");
        calendario.setCodigo(codigo);

        Usuario usuario = new Usuario();
        usuario.setId(idUser);

        when(calendarioRepository.findByCodigo(codigo))
                .thenReturn(Optional.of(calendario));

        when(usuarioRepository.findById(idUser))
                .thenReturn(Optional.of(usuario));

        CalendarioSimpleDTO resultado =
                calendarioService.unirseCalendario(codigo, idUser);

        assertNotNull(resultado);
        assertEquals(5, resultado.getIdCal());
        assertEquals("Calendario Compartido", resultado.getNombre());
        assertEquals("ABC123", resultado.getCodigo());
        assertEquals(RolUsuarioCalendario.member, resultado.getRol());

        verify(relUserCalRepository).save(any(RelUserCal.class));
    }

    @Test
    void unirseCalendario_calendarioNoExiste() {
        String codigo = "NO_EXISTE";
        Integer idUser = 1;

        when(calendarioRepository.findByCodigo(codigo))
                .thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> calendarioService.unirseCalendario(codigo, idUser)
        );

        assertEquals(404, ex.getStatusCode().value());

        verify(relUserCalRepository, never()).save(any());
    }
}