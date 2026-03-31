package com.inicio.back_end.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inicio.back_end.dto.DTOUsuario;
import com.inicio.back_end.model.Usuario;
import com.inicio.back_end.model.enums.RoleUsuario;
import com.inicio.back_end.repository.RepositoryUsuario;
import com.inicio.back_end.service.ServiceToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Testes de integração para AutenticacaoController
 * Testa endpoints de login com autenticação
 */
@SpringBootTest
@AutoConfigureWebMvc
@TestPropertySource(locations = "classpath:application-test.properties")
class AutenticacaoControllerTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private ServiceToken serviceToken;

    @MockBean
    private RepositoryUsuario repositoryUsuario;

    @MockBean
    private PasswordEncoder passwordEncoder;

    private MockMvc mockMvc;
    private Usuario usuario;
    private DTOUsuario dtoUsuario;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setLogin("teste_usuario");
        usuario.setSenha("senha_criptografada");
        usuario.setRole(RoleUsuario.CLIENTE);

        dtoUsuario = new DTOUsuario("teste_usuario", "senha123");
    }

    @Test
    void deveFazerLoginComSucesso() throws Exception {
        // Given
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(usuario);
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(serviceToken.gerarToken(dtoUsuario)).thenReturn("token_jwt_teste");

        // When & Then
        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dtoUsuario)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Login realizado com sucesso"))
                .andExpect(jsonPath("$.data.token").value("token_jwt_teste"))
                .andExpect(jsonPath("$.data.usuario.id").value(1))
                .andExpect(jsonPath("$.data.usuario.login").value("teste_usuario"));
    }

    @Test
    void deveRetornarErroParaCredenciaisInvalidas() throws Exception {
        // Given
        when(authenticationManager.authenticate(any()))
                .thenThrow(new BadCredentialsException("Credenciais inválidas"));

        // When & Then
        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dtoUsuario)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Usuário ou senha incorretos"));
    }

    @Test
    void deveRetornarErroParaDadosInvalidos() throws Exception {
        // Given - DTO com dados inválidos
        DTOUsuario dtoInvalido = new DTOUsuario("", "");

        // When & Then
        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dtoInvalido)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornarErroParaJsonInvalido() throws Exception {
        // When & Then
        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("json_invalido"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornarErroInternoQuandoServicoFalha() throws Exception {
        // Given
        when(authenticationManager.authenticate(any()))
                .thenThrow(new RuntimeException("Erro interno"));

        // When & Then
        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dtoUsuario)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void deveValidarCamposObrigatorios() throws Exception {
        // Given - DTO sem login
        DTOUsuario dtoSemLogin = new DTOUsuario(null, "senha123");

        // When & Then
        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dtoSemLogin)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveAceitarApenasPostRequests() throws Exception {
        // When & Then - Tentar GET no endpoint de login
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/auth/login"))
                .andExpect(status().isMethodNotAllowed());
    }
}
