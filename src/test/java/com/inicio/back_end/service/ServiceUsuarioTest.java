package com.inicio.back_end.service;

import com.inicio.back_end.dto.DTOUsuario;
import com.inicio.back_end.model.Usuario;
import com.inicio.back_end.model.enums.RoleUsuario;
import com.inicio.back_end.repository.RepositoryUsuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para ServiceUsuario
 * Testa registro de usuários e criptografia de senhas
 */
@ExtendWith(MockitoExtension.class)
class ServiceUsuarioTest {

    @InjectMocks
    private ServiceUsuario serviceUsuario;

    @Mock
    private RepositoryUsuario repositoryUsuario;

    @Mock
    private PasswordEncoder passwordEncoder;

    private DTOUsuario dtoUsuario;

    @BeforeEach
    void setUp() {
        dtoUsuario = new DTOUsuario("novo_usuario", "senha123");
    }

    @Test
    void deveRegistrarUsuarioComSucesso() {
        // Given
        when(repositoryUsuario.findByLogin(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("senha_criptografada");
        when(repositoryUsuario.save(any(Usuario.class))).thenAnswer(invocation -> {
            Usuario usuario = invocation.getArgument(0);
            usuario.setId(1L);
            return usuario;
        });

        // When
        Usuario usuarioRegistrado = serviceUsuario.registrarUsuario(dtoUsuario);

        // Then
        assertNotNull(usuarioRegistrado);
        assertEquals("novo_usuario", usuarioRegistrado.getUsername());
        assertEquals("senha_criptografada", usuarioRegistrado.getPassword());
        assertEquals(RoleUsuario.CLIENTE, usuarioRegistrado.getRole());
        assertEquals(1L, usuarioRegistrado.getId());

        verify(repositoryUsuario).findByLogin("novo_usuario");
        verify(passwordEncoder).encode("senha123");
        verify(repositoryUsuario).save(any(Usuario.class));
    }

    @Test
    void deveLancarExcecaoQuandoUsuarioJaExiste() {
        // Given
        Usuario usuarioExistente = new Usuario();
        usuarioExistente.setId(1L);
        usuarioExistente.setLogin("novo_usuario");

        when(repositoryUsuario.findByLogin("novo_usuario")).thenReturn(Optional.of(usuarioExistente));

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            serviceUsuario.registrarUsuario(dtoUsuario);
        });

        assertTrue(exception.getMessage().contains("Usuário com este login já existe"));
        verify(repositoryUsuario, never()).save(any(Usuario.class));
        verify(passwordEncoder, never()).encode(anyString());
    }

    @Test
    void deveBuscarUsuarioPorLogin() {
        // Given
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setLogin("teste_usuario");

        when(repositoryUsuario.findByLogin("teste_usuario")).thenReturn(Optional.of(usuario));

        // When
        Optional<Usuario> resultado = serviceUsuario.buscarPorLogin("teste_usuario");

        // Then
        assertTrue(resultado.isPresent());
        assertEquals("teste_usuario", resultado.get().getUsername());
        verify(repositoryUsuario).findByLogin("teste_usuario");
    }

    @Test
    void deveRetornarOptionalVazioQuandoUsuarioNaoEncontrado() {
        // Given
        when(repositoryUsuario.findByLogin("usuario_inexistente")).thenReturn(Optional.empty());

        // When
        Optional<Usuario> resultado = serviceUsuario.buscarPorLogin("usuario_inexistente");

        // Then
        assertFalse(resultado.isPresent());
        verify(repositoryUsuario).findByLogin("usuario_inexistente");
    }

    @Test
    void deveBuscarUsuarioPorId() {
        // Given
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setLogin("teste_usuario");

        when(repositoryUsuario.findById(1L)).thenReturn(Optional.of(usuario));

        // When
        Optional<Usuario> resultado = serviceUsuario.buscarPorId(1L);

        // Then
        assertTrue(resultado.isPresent());
        assertEquals(1L, resultado.get().getId());
        verify(repositoryUsuario).findById(1L);
    }

    @Test
    void deveLancarExcecaoQuandoRepositoryFalha() {
        // Given
        when(repositoryUsuario.findByLogin(anyString())).thenThrow(new RuntimeException("Erro no banco"));

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            serviceUsuario.registrarUsuario(dtoUsuario);
        });

        assertTrue(exception.getMessage().contains("Erro no banco"));
    }
}
