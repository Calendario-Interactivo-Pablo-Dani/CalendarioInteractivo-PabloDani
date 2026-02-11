package com.planify.api.Service;

import com.planify.api.POJOs.*;
import com.planify.api.dto.*;
import com.planify.api.enums.*;
import com.planify.api.repository.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReluserCalServiceTest {
    @Mock
    private RelUserCalRepository relUserCalRepository;

    @Mock
    private CalendarioRepository calendarioRepository;

    @InjectMocks
    private ReluserCalService reluserCalService;

    //OBTENER RELACIONES DE USUARIO
    @Test
    void obtenerRelacionesDeUsuario_correcto() {
        Integer idUser = 1;

        Calendario calendario = new Calendario();
        calendario.setId(10);
        calendario.setNombre("Calendario DAM");
        calendario.setCodigo("ABC123");

        RelUserCal relacion = new RelUserCal();
        relacion.setIdCal(calendario);
        relacion.setRol(RolUsuarioCalendario.owner);

        when(relUserCalRepository.findByIdUser_Id(idUser))
                .thenReturn(List.of(relacion));

        List<CalendarioSimpleDTO> resultado =
                reluserCalService.obtenerRelacionesDeUsuario(idUser);

        assertEquals(1, resultado.size());

        CalendarioSimpleDTO dto = resultado.get(0);
        assertEquals(10, dto.getIdCal());
        assertEquals("Calendario DAM", dto.getNombre());
        assertEquals("ABC123", dto.getCodigo());
        assertEquals(RolUsuarioCalendario.owner, dto.getRol());
    }

    @Test
    void obtenerRelacionesDeUsuario_sinRelaciones() {
        when(relUserCalRepository.findByIdUser_Id(anyInt()))
                .thenReturn(List.of());

        List<CalendarioSimpleDTO> resultado =
                reluserCalService.obtenerRelacionesDeUsuario(1);

        assertTrue(resultado.isEmpty());
    }

    //OBTENER MIEMBROS DE CALENDARIO
    @Test
    void obtenerMiembros_correcto() {
        Integer idCal = 5;

        Usuario usuario = new Usuario();
        usuario.setId(1);
        usuario.setNombre("Daniel");
        usuario.setEmail("daniel@test.com");
        usuario.setUsername("dani");

        RelUserCal relacion = new RelUserCal();
        relacion.setIdUser(usuario);
        relacion.setRol(RolUsuarioCalendario.member);

        when(relUserCalRepository.findByIdCal_Id(idCal))
                .thenReturn(List.of(relacion));

        List<UsuarioCalendarioDTO> resultado =
                reluserCalService.obtenerMiembros(idCal);

        assertEquals(1, resultado.size());

        UsuarioCalendarioDTO dto = resultado.get(0);
        assertEquals(1, dto.getIdUser());
        assertEquals("Daniel", dto.getNombre());
        assertEquals("daniel@test.com", dto.getEmail());
        assertEquals("member", dto.getRol());
        assertEquals("dani", dto.getUsername());
    }

    @Test
    void obtenerMiembros_sinMiembros() {
        when(relUserCalRepository.findByIdCal_Id(anyInt()))
                .thenReturn(List.of());

        List<UsuarioCalendarioDTO> resultado =
                reluserCalService.obtenerMiembros(5);

        assertTrue(resultado.isEmpty());
    }

    //OBTENER TOTAL DE CALENDARIOS
    @Test
    void obtenerTotalCalendarios_correcto() {
        when(relUserCalRepository.countByIdUser_Id(1))
                .thenReturn(3);

        int total = reluserCalService.obtenerTotalCalendarios(1);

        assertEquals(3, total);
    }






}