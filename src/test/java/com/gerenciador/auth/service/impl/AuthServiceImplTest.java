package com.gerenciador.auth.service.impl;

import com.gerenciador.auth.entity.Usuario;
import com.gerenciador.auth.enums.Role;
import com.gerenciador.auth.jwt.JwtService;
import com.gerenciador.auth.mapper.UsuarioMapper;
import com.gerenciador.auth.record.LoginResponseRecord;
import com.gerenciador.auth.record.LoginResquetRecord;
import com.gerenciador.auth.record.RegisterRequestRecord;
import com.gerenciador.auth.record.UsuarioLogadoRecord;
import com.gerenciador.auth.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceImplTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private UsuarioMapper mapper;

    @InjectMocks
    private AuthServiceImpl authService;

    private RegisterRequestRecord registerRequest;
    private LoginResquetRecord loginRequest;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        registerRequest = new RegisterRequestRecord("user123", "user@mail.com", Role.ADMIN);
        loginRequest = new LoginResquetRecord("user123", "123456");
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setUsername("user123");
        usuario.setPassword("encodedPass");
    }

    @Test
    void deveRegistrarUsuarioComSucesso() {
        when(mapper.toUsuario(registerRequest)).thenReturn(usuario);
        when(passwordEncoder.encode("123456")).thenReturn("encodedPass");

        authService.register(registerRequest);

        verify(usuarioRepository).save(usuario);
    }

    @Test
    void deveFazerLoginComSucesso() {
        when(usuarioRepository.findByUsername("user123")).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("123456", "encodedPass")).thenReturn(true);
        when(jwtService.generateToken(usuario)).thenReturn("token123");

        LoginResponseRecord response = authService.login(loginRequest);

        assertNotNull(response);
        assertEquals("token123", response.token());
        verify(jwtService).generateToken(usuario);
    }

    @Test
    void deveLancarExcecaoQuandoUsuarioNaoEncontrado() {
        when(usuarioRepository.findByUsername("user123")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> authService.login(loginRequest));
    }

    @Test
    void deveLancarExcecaoQuandoSenhaInvalida() {
        when(usuarioRepository.findByUsername("user123")).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("123456", "encodedPass")).thenReturn(false);

        assertThrows(BadCredentialsException.class, () -> authService.login(loginRequest));
    }

    @Test
    void deveRetornarUsuarioLogadoComSucesso() {
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        User userDetails = new User("user123", "password", List.of(() -> "ROLE_USER"));

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        SecurityContextHolder.setContext(securityContext);

        UsuarioLogadoRecord usuarioLogado = authService.getUsuarioLogado();

        assertNotNull(usuarioLogado);
        assertEquals("user123", usuarioLogado.username());
        assertEquals("ROLE_USER", usuarioLogado.role());
    }

    @Test
    void deveLancarExcecaoQuandoUsuarioNaoAutenticado() {
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn("anonymousUser");
        SecurityContextHolder.setContext(securityContext);

        assertThrows(RuntimeException.class, () -> authService.getUsuarioLogado());
    }
}
