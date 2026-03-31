package com.inicio.back_end.service;

import com.inicio.back_end.model.Usuario;
import com.inicio.back_end.model.enums.RoleUsuario;
import com.inicio.back_end.repository.RepositoryUsuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para ServiceAutenticacao
 * Testa carregamento de usuários por username
 */
@ExtendWith(MockitoExtension.class)
class ServiceAutenticacaoTest {

    @InjectMocks
    private ServiceAutenticacao serviceAutenticacao;

    @Mock
    private RepositoryUsuario repositoryUsuario;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setLogin("teste_usuario");
        usuario.setSenha("senha_criptografada");
        usuario.setRole(RoleUsuario.CLIENTE);
    }

    @Test
    void deveCarregarUsuarioPorUsernameComSucesso() {
        // Given
        when(repositoryUsuario.findByLogin("teste_usuario")).thenReturn(Optional.of(usuario));

        // When
        var userDetails = serviceAutenticacao.loadUserByUsername("teste_usuario");

        // Then
        assertNotNull(userDetails);
        assertEquals("teste_usuario", userDetails.getUsername());
        assertEquals("senha_criptografada", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_CLIENTE")));
        assertTrue(userDetails.isAccountNonExpired());
        assertTrue(userDetails.isAccountNonLocked());
        assertTrue(userDetails.isCredentialsNonExpired());
        assertTrue(userDetails.isEnabled());

        verify(repositoryUsuario).findByLogin("teste_usuario");
    }

    @Test
    void deveLancarExcecaoQuandoUsuarioNaoEncontrado() {
        // Given
        when(repositoryUsuario.findByLogin("usuario_inexistente")).thenReturn(Optional.empty());

        // When & Then
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
            serviceAutenticacao.loadUserByUsername("usuario_inexistente");
        });

        assertTrue(exception.getMessage().contains("Usuário não encontrado: usuario_inexistente"));
        verify(repositoryUsuario).findByLogin("usuario_inexistente");
    }

    @Test
    void deveLancarExcecaoQuandoRepositoryFalha() {
        // Given
        when(repositoryUsuario.findByLogin(anyString())).thenThrow(new RuntimeException("Erro no banco"));

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            serviceAutenticacao.loadUserByUsername("teste_usuario");
        });

        assertTrue(exception.getMessage().contains("Erro no banco"));
    }

    @Test
    void deveRetornarAuthoritiesCorretasParaDiferentesRoles() {
        // Teste ADMIN
        usuario.setRole(RoleUsuario.ADMIN);
        when(repositoryUsuario.findByLogin("admin")).thenReturn(Optional.of(usuario));

        var adminUser = serviceAutenticacao.loadUserByUsername("admin");
        assertTrue(adminUser.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN")));

        // Teste BARBEIRO
        usuario.setRole(RoleUsuario.BARBEIRO);
        when(repositoryUsuario.findByLogin("barbeiro")).thenReturn(Optional.of(usuario));

        var barbeiroUser = serviceAutenticacao.loadUserByUsername("barbeiro");
        assertTrue(barbeiroUser.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_BARBEIRO")));

        // Teste RECEPCIONISTA
        usuario.setRole(RoleUsuario.RECEPCIONISTA);
        when(repositoryUsuario.findByLogin("recepcionista")).thenReturn(Optional.of(usuario));

        var recepcionistaUser = serviceAutenticacao.loadUserByUsername("recepcionista");
        assertTrue(recepcionistaUser.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_RECEPCIONISTA")));
    }

    @Test
    void deveImplementarTodosMetodosUserDetails() {
        // Given
        when(repositoryUsuario.findByLogin("teste_usuario")).thenReturn(Optional.of(usuario));

        // When
        var userDetails = serviceAutenticacao.loadUserByUsername("teste_usuario");

        // Then - Verificar implementação completa de UserDetails
        assertNotNull(userDetails.getUsername());
        assertNotNull(userDetails.getPassword());
        assertNotNull(userDetails.getAuthorities());
        assertTrue(userDetails.isAccountNonExpired());
        assertTrue(userDetails.isAccountNonLocked());
        assertTrue(userDetails.isCredentialsNonExpired());
        assertTrue(userDetails.isEnabled());
    }
}
