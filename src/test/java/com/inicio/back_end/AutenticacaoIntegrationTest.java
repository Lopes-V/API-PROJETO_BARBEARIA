package com.inicio.back_end;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inicio.back_end.dto.DTOUsuario;
import com.inicio.back_end.model.Usuario;
import com.inicio.back_end.model.enums.RoleUsuario;
import com.inicio.back_end.repository.RepositoryUsuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Testes de integração completa para o fluxo de autenticação
 * Testa o fluxo completo: registro → login → acesso protegido
 */
@SpringBootTest
@AutoConfigureWebMvc
class AutenticacaoIntegrationTest {

        @Autowired
        private WebApplicationContext context;

        @Autowired
        private ObjectMapper objectMapper;

        @Autowired
        private RepositoryUsuario repositoryUsuario;

        @Autowired
        private PasswordEncoder passwordEncoder;

        private MockMvc mockMvc;

        @BeforeEach
        void setUp() {
                mockMvc = MockMvcBuilders
                                .webAppContextSetup(context)
                                .apply(springSecurity())
                                .build();

                // Limpar dados de teste
                repositoryUsuario.deleteAll();
        }

        @Test
        void deveExecutarFluxoCompletoDeAutenticacao() throws Exception {
                // Verificar que usuário foi salvo no banco
                Usuario usuarioSalvo = repositoryUsuario.findByLogin("usuario_teste").orElse(null);
                assertNotNull(usuarioSalvo);
                assertEquals("usuario_teste", usuarioSalvo.getUsername());
                assertEquals(RoleUsuario.CLIENTE, usuarioSalvo.getRole());
                assertTrue(passwordEncoder.matches("senha123456", usuarioSalvo.getPassword()));

                // === PASSO 2: Fazer login ===
                DTOUsuario dtoLogin = new DTOUsuario("usuario_teste", "senha123456");

                MvcResult resultadoLogin = mockMvc.perform(post("/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dtoLogin)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.success").value(true))
                                .andExpect(jsonPath("$.message").value("Login realizado com sucesso"))
                                .andExpect(jsonPath("$.data.token").exists())
                                .andExpect(jsonPath("$.data.usuario.login").value("usuario_teste"))
                                .andReturn();

                // Extrair token da resposta
                String responseBody = resultadoLogin.getResponse().getContentAsString();
                Map<String, Object> responseMap = objectMapper.readValue(responseBody, Map.class);
                @SuppressWarnings("unchecked")
                Map<String, Object> data = (Map<String, Object>) responseMap.get("data");
                String token = (String) data.get("token");

                assertNotNull(token);
                assertTrue(token.startsWith("eyJ")); // Deve ser um JWT

                // === PASSO 3: Acessar endpoint protegido com token ===
                mockMvc.perform(get("/usuarios/me")
                                .header("Authorization", "Bearer " + token))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.success").value(true))
                                .andExpect(jsonPath("$.message").value("Dados do usuário recuperados com sucesso"))
                                .andExpect(jsonPath("$.data.login").value("usuario_teste"))
                                .andExpect(jsonPath("$.data.id").value(usuarioSalvo.getId().intValue()));

                // === PASSO 4: Tentar acessar sem token (deve falhar) ===
                mockMvc.perform(get("/usuarios/me"))
                                .andExpect(status().isUnauthorized());

                // === PASSO 5: Tentar login com senha errada (deve falhar) ===
                DTOUsuario dtoLoginErrado = new DTOUsuario("usuario_teste", "senha_errada");

                mockMvc.perform(post("/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dtoLoginErrado)))
                                .andExpect(status().isUnauthorized())
                                .andExpect(jsonPath("$.success").value(false))
                                .andExpect(jsonPath("$.message").value("Usuário ou senha incorretos"));
        }

        @Test
        void deveRejeitarRegistroDeUsuarioDuplicado() throws Exception {
                // === PASSO 1: Registrar primeiro usuário ===
                DTOUsuario dtoUsuario = new DTOUsuario("usuario_unico", "senha123");

                mockMvc.perform(post("/usuarios/registrar")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dtoUsuario)))
                                .andExpect(status().isCreated());

                // === PASSO 2: Tentar registrar usuário com mesmo login ===
                mockMvc.perform(post("/usuarios/registrar")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dtoUsuario)))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.success").value(false))
                                .andExpect(jsonPath("$.message")
                                                .value("Erro ao registrar usuário: Usuário com este login já existe!"));
        }

        @Test
        void deveValidarCamposObrigatorios() throws Exception {
                // === PASSO 1: Tentar registrar sem login ===
                DTOUsuario dtoSemLogin = new DTOUsuario(null, "senha123");

                mockMvc.perform(post("/usuarios/registrar")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dtoSemLogin)))
                                .andExpect(status().isBadRequest());

                // === PASSO 2: Tentar registrar sem senha ===
                DTOUsuario dtoSemSenha = new DTOUsuario("login_teste", null);

                mockMvc.perform(post("/usuarios/registrar")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dtoSemSenha)))
                                .andExpect(status().isBadRequest());

                // === PASSO 3: Tentar login sem dados ===
                DTOUsuario dtoVazio = new DTOUsuario("", "");

                mockMvc.perform(post("/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dtoVazio)))
                                .andExpect(status().isBadRequest());
        }

        @Test
        void deveRejeitarJsonInvalido() throws Exception {
                // === PASSO 1: JSON inválido no registro ===
                mockMvc.perform(post("/usuarios/registrar")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("json_invalido"))
                                .andExpect(status().isBadRequest());

                // === PASSO 2: JSON inválido no login ===
                mockMvc.perform(post("/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("json_invalido"))
                                .andExpect(status().isBadRequest());
        }

        @Test
        void deveRejeitarMetodosHttpIncorretos() throws Exception {
                // === PASSO 1: GET no endpoint de registro ===
                mockMvc.perform(get("/usuarios/registrar"))
                                .andExpect(status().isMethodNotAllowed());

                // === PASSO 2: POST no endpoint /me ===
                mockMvc.perform(post("/usuarios/me"))
                                .andExpect(status().isMethodNotAllowed());
        }

        @Test
        void deveGerenciarMultiplosUsuarios() throws Exception {
                // === PASSO 1: Registrar múltiplos usuários ===
                DTOUsuario usuario1 = new DTOUsuario("usuario1", "senha1");
                DTOUsuario usuario2 = new DTOUsuario("usuario2", "senha2");

                // Registrar ambos
                mockMvc.perform(post("/usuarios/registrar")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(usuario1)))
                                .andExpect(status().isCreated());

                mockMvc.perform(post("/usuarios/registrar")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(usuario2)))
                                .andExpect(status().isCreated());

                // === PASSO 2: Login com usuário 1 ===
                MvcResult login1 = mockMvc.perform(post("/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(usuario1)))
                                .andExpect(status().isOk())
                                .andReturn();

                String token1 = extrairToken(login1);

                // === PASSO 3: Login com usuário 2 ===
                MvcResult login2 = mockMvc.perform(post("/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(usuario2)))
                                .andExpect(status().isOk())
                                .andReturn();

                String token2 = extrairToken(login2);

                // === PASSO 4: Verificar isolamento ===
                // Usuário 1 acessando com seu token
                mockMvc.perform(get("/usuarios/me")
                                .header("Authorization", "Bearer " + token1))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.data.login").value("usuario1"));

                // Usuário 2 acessando com seu token
                mockMvc.perform(get("/usuarios/me")
                                .header("Authorization", "Bearer " + token2))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.data.login").value("usuario2"));

                // Verificar que são usuários diferentes
                assertNotEquals(token1, token2);
        }

        /**
         * Método auxiliar para extrair token da resposta de login
         */
        private String extrairToken(MvcResult resultadoLogin) throws Exception {
                String responseBody = resultadoLogin.getResponse().getContentAsString();
                Map<String, Object> responseMap = objectMapper.readValue(responseBody, Map.class);
                @SuppressWarnings("unchecked")
                Map<String, Object> data = (Map<String, Object>) responseMap.get("data");
                return (String) data.get("token");
        }
}
