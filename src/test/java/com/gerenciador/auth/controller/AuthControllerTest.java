package com.gerenciador.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gerenciador.auth.enums.Role;
import com.gerenciador.auth.record.LoginResponseRecord;
import com.gerenciador.auth.record.LoginResquetRecord;
import com.gerenciador.auth.record.RegisterRequestRecord;
import com.gerenciador.auth.service.AuthService;
import com.gerenciador.security.JwtFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SuppressWarnings("removal")
@WebMvcTest(
        controllers = AuthController.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = JwtFilter.class
        )
)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @Autowired
    private ObjectMapper objectMapper;

    private RegisterRequestRecord registerRequest;
    private LoginResquetRecord loginRequest;
    private LoginResponseRecord loginResponse;

    @BeforeEach
    void setUp() {
        registerRequest = new RegisterRequestRecord("user123", "user@mail.com", Role.ADMIN);
        loginRequest = new LoginResquetRecord("user@mail.com", "123456");
        loginResponse = new LoginResponseRecord("token123");
    }

    @Test
    void deveRegistrarUsuarioComSucesso() throws Exception {
        doNothing().when(authService).register(any(RegisterRequestRecord.class));

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isCreated());

        // Nenhuma resposta de corpo é esperada
    }

    @Test
    void deveFazerLoginComSucesso() throws Exception {
        when(authService.login(any(LoginResquetRecord.class))).thenReturn(loginResponse);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("token123"));
    }
}
