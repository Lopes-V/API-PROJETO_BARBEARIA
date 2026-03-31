package com.inicio.back_end.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inicio.back_end.dto.DTOUsuario;
import com.inicio.back_end.model.Usuario;
import com.inicio.back_end.model.enums.RoleUsuario;
import com.inicio.back_end.service.ServiceUsuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Testes de integração para UsuarioController
 * Testa endpoints de registro e dados do usuário
 */
@SpringBootTest
@AutoConfigureWebMvc
@TestPropertySource(locations = "classpath:application-test.properties")
class UsuarioControllerTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ServiceUsuario serviceUsuario;

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

        dtoUsuario = new DTOUsuario("novo_usuario", "senha123");
    }

    @Test
    void deveRegistrarUsuarioComSucesso() throws Exception {
        // Given
        Usuario usuarioRegistrado = new Usuario();
        usuarioRegistrado.setId(2L);
        usuarioRegistrado.setLogin("novo_usuario");
        usuarioRegistrado.setRole(RoleUsuario.CLIENTE);

        when(serviceUsuario.registrarUsuario(any(DTOUsuario.class))).thenReturn(usuarioRegistrado);

        // When & Then
        mockMvc.perform(post("/usuarios/registrar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dtoUsuario)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Usuário registrado com sucesso"))
                .andExpect(jsonPath("$.data.id").value(2))
                .andExpect(jsonPath("$.data.login").value("novo_usuario"));
    }

    @Test
    void deveRetornarErroQuandoUsuarioJaExiste() throws Exception {
        // Given
        when(serviceUsuario.registrarUsuario(any(DTOUsuario.class)))
                .thenThrow(new RuntimeException("Usuário com este login já existe!"));

        // When & Then
        mockMvc.perform(post("/usuarios/registrar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dtoUsuario)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Erro ao registrar usuário: Usuário com este login já existe!"));
    }

    @Test
    @WithMockUser(username = "teste_usuario", roles = {"CLIENTE"})
    void deveRetornarDadosDoUsuarioAutenticado() throws Exception {
        // Given
        when(serviceUsuario.buscarPorLogin("teste_usuario")).thenReturn(Optional.of(usuario));

        // When & Then
        mockMvc.perform(get("/usuarios/me"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Dados do usuário recuperados com sucesso"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.login").value("teste_usuario"));
    }

    @Test
    void deveRetornarErroQuandoUsuarioNaoAutenticado() throws Exception {
        // When & Then
        mockMvc.perform(get("/usuarios/me"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void deveRetornarErroParaDadosInvalidosNoRegistro() throws Exception {
        // Given - DTO com dados inválidos
        DTOUsuario dtoInvalido = new DTOUsuario("", "");

        // When & Then
        mockMvc.perform(post("/usuarios/registrar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dtoInvalido)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornarErroParaJsonInvalido() throws Exception {
        // When & Then
        mockMvc.perform(post("/usuarios/registrar")
                .contentType(MediaType.APPLICATION_JSON)
                .content("json_invalido"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveValidarCamposObrigatoriosNoRegistro() throws Exception {
        // Given - DTO sem login
        DTOUsuario dtoSemLogin = new DTOUsuario(null, "senha123");

        // When & Then
        mockMvc.perform(post("/usuarios/registrar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dtoSemLogin)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveAceitarApenasPostParaRegistro() throws Exception {
        // When & Then - Tentar GET no endpoint de registro
        mockMvc.perform(get("/usuarios/registrar"))
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    void deveAceitarApenasGetParaMe() throws Exception {
        // When & Then - Tentar POST no endpoint /me
        mockMvc.perform(post("/usuarios/me"))
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    @WithMockUser(username = "teste_usuario", roles = {"CLIENTE"})
    void deveRetornarErroQuandoServicoFalha() throws Exception {
        // Given
        when(serviceUsuario.buscarPorLogin(anyString()))
                .thenThrow(new RuntimeException("Erro interno do serviço"));

        // When & Then
        mockMvc.perform(get("/usuarios/me"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void devePermitirAdminAcessarDados() throws Exception {
        // Given - Usuário com role ADMIN
        Usuario adminUser = new Usuario();
        adminUser.setId(1L);
        adminUser.setLogin("admin");
        adminUser.setRole(RoleUsuario.ADMIN);

        when(serviceUsuario.buscarPorLogin("admin")).thenReturn(Optional.of(adminUser));

        // When & Then
        mockMvc.perform(get("/usuarios/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.login").value("admin"));
    }
}
