package com.planify.api.Service;

import com.planify.api.POJOs.Usuario;
import com.planify.api.dto.*;
import com.planify.api.repository.UsuarioRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    //LOGIN
    @Test
    void login_correcto() {
        LoginRequestDTO request = new LoginRequestDTO();
        request.setEmail("test@mail.com");
        request.setPassword("1234");

        Usuario usuario = new Usuario();
        usuario.setId(1);
        usuario.setEmail("test@mail.com");
        usuario.setPasswordHash("HASH");

        when(usuarioRepository.findByEmail("test@mail.com"))
                .thenReturn(Optional.of(usuario));

        when(passwordEncoder.matches("1234", "HASH"))
                .thenReturn(true);

        Usuario resultado = authService.login(request);

        assertNotNull(resultado);
        assertEquals(1, resultado.getId());
    }

    @Test
    void login_emailNoExiste() {
        LoginRequestDTO request = new LoginRequestDTO();
        request.setEmail("no@mail.com");
        request.setPassword("1234");

        when(usuarioRepository.findByEmail("no@mail.com"))
                .thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> authService.login(request)
        );

        assertEquals("Credenciales incorrectas", ex.getMessage());
    }

    @Test
    void login_passwordIncorrecta() {
        LoginRequestDTO request = new LoginRequestDTO();
        request.setEmail("test@mail.com");
        request.setPassword("wrong");

        Usuario usuario = new Usuario();
        usuario.setEmail("test@mail.com");
        usuario.setPasswordHash("HASH");

        when(usuarioRepository.findByEmail("test@mail.com"))
                .thenReturn(Optional.of(usuario));

        when(passwordEncoder.matches("wrong", "HASH"))
                .thenReturn(false);

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> authService.login(request)
        );

        assertEquals("Credenciales incorrectas", ex.getMessage());
    }

    //REGISTER
    @Test
    void register_correcto() {
        RegisterRequestDTO request = new RegisterRequestDTO();
        request.setNombre("Daniel");
        request.setUsername("dani");
        request.setEmail("daniel@mail.com");
        request.setPassword("1234");
        request.setTelefono("123456789");

        when(usuarioRepository.findByEmail("daniel@mail.com"))
                .thenReturn(Optional.empty());

        when(usuarioRepository.findByUsername("dani"))
                .thenReturn(Optional.empty());

        when(passwordEncoder.encode("1234"))
                .thenReturn("HASH");

        when(usuarioRepository.save(any(Usuario.class)))
                .thenAnswer(invocation -> {
                    Usuario u = invocation.getArgument(0);
                    u.setId(10);
                    return u;
                });

        Usuario resultado = authService.register(request);

        assertNotNull(resultado);
        assertEquals(10, resultado.getId());
        assertEquals("HASH", resultado.getPasswordHash());
    }

    @Test
    void register_emailDuplicado() {
        RegisterRequestDTO request = new RegisterRequestDTO();
        request.setEmail("test@mail.com");

        when(usuarioRepository.findByEmail("test@mail.com"))
                .thenReturn(Optional.of(new Usuario()));

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> authService.register(request)
        );

        assertEquals("El email ya está registrado", ex.getMessage());
    }

    @Test
    void register_usernameDuplicado() {
        RegisterRequestDTO request = new RegisterRequestDTO();
        request.setEmail("test@mail.com");
        request.setUsername("dani");

        when(usuarioRepository.findByEmail("test@mail.com"))
                .thenReturn(Optional.empty());

        when(usuarioRepository.findByUsername("dani"))
                .thenReturn(Optional.of(new Usuario()));

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> authService.register(request)
        );

        assertEquals("El username ya está en uso", ex.getMessage());
    }










}