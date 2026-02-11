package com.planify.api.Service;

import com.planify.api.POJOs.Usuario;
import com.planify.api.repository.UsuarioRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {
    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    //FIND ALL
    @Test
    void findAll_correcto() {
        Usuario u1 = new Usuario();
        u1.setId(1);

        Usuario u2 = new Usuario();
        u2.setId(2);

        when(usuarioRepository.findAll())
                .thenReturn(List.of(u1, u2));

        List<Usuario> resultado = usuarioService.findAll();

        assertEquals(2, resultado.size());
        verify(usuarioRepository).findAll();
    }

    //FIND BY ID
    @Test
    void findById_correcto() {
        Usuario usuario = new Usuario();
        usuario.setId(1);

        when(usuarioRepository.findById(1))
                .thenReturn(Optional.of(usuario));

        Usuario resultado = usuarioService.findById(1);

        assertNotNull(resultado);
        assertEquals(1, resultado.getId());
    }

    @Test
    void findById_usuarioNoExiste() {
        when(usuarioRepository.findById(99))
                .thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> usuarioService.findById(99)
        );

        assertEquals("Usuario no encontrado", ex.getMessage());
    }






}