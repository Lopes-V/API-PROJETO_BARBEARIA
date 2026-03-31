package com.inicio.back_end.config;

import com.inicio.back_end.model.Usuario;
import com.inicio.back_end.model.enums.RoleUsuario;
import com.inicio.back_end.service.ServiceAutenticacao;
import com.inicio.back_end.service.ServiceToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para JwtAuthenticationFilter
 * Testa validação de tokens JWT no filtro
 */
@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Mock
    private ServiceToken serviceToken;

    @Mock
    private ServiceAutenticacao serviceAutenticacao;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @Mock
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        // Limpar contexto de segurança antes de cada teste
        SecurityContextHolder.clearContext();

        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setLogin("teste_usuario");
        usuario.setRole(RoleUsuario.CLIENTE);
    }

    @Test
    void deveProcessarRequisicaoComTokenValido() throws Exception {
        // Given
        String token = "valid.jwt.token";
        String login = "teste_usuario";

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(serviceToken.getToken(token)).thenReturn(login);
        when(serviceAutenticacao.loadUserByUsername(login)).thenReturn(usuario);

        // When
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Then
        verify(serviceToken).getToken(token);
        verify(serviceAutenticacao).loadUserByUsername(login);
        verify(filterChain).doFilter(request, response);

        // Verificar que autenticação foi definida
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        assertEquals(login, SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @Test
    void deveProcessarRequisicaoSemToken() throws Exception {
        // Given
        when(request.getHeader("Authorization")).thenReturn(null);

        // When
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Then
        verify(serviceToken, never()).getToken(anyString());
        verify(serviceAutenticacao, never()).loadUserByUsername(anyString());
        verify(filterChain).doFilter(request, response);

        // Verificar que não há autenticação
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void deveProcessarRequisicaoComHeaderAuthorizationInvalido() throws Exception {
        // Given - Header sem "Bearer"
        when(request.getHeader("Authorization")).thenReturn("InvalidHeader");

        // When
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Then
        verify(serviceToken, never()).getToken(anyString());
        verify(serviceAutenticacao, never()).loadUserByUsername(anyString());
        verify(filterChain).doFilter(request, response);

        // Verificar que não há autenticação
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void deveProcessarRequisicaoComTokenInvalido() throws Exception {
        // Given
        String tokenInvalido = "invalid.jwt.token";

        when(request.getHeader("Authorization")).thenReturn("Bearer " + tokenInvalido);
        when(serviceToken.getToken(tokenInvalido))
                .thenThrow(new RuntimeException("Token inválido"));

        // When
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Then
        verify(serviceToken).getToken(tokenInvalido);
        verify(serviceAutenticacao, never()).loadUserByUsername(anyString());
        verify(filterChain).doFilter(request, response);

        // Verificar que não há autenticação (token inválido não define contexto)
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void deveProcessarRequisicaoQuandoUsuarioNaoEncontrado() throws Exception {
        // Given
        String token = "valid.jwt.token";
        String login = "usuario_inexistente";

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(serviceToken.getToken(token)).thenReturn(login);
        when(serviceAutenticacao.loadUserByUsername(login))
                .thenThrow(new RuntimeException("Usuário não encontrado"));

        // When
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Then
        verify(serviceToken).getToken(token);
        verify(serviceAutenticacao).loadUserByUsername(login);
        verify(filterChain).doFilter(request, response);

        // Verificar que não há autenticação
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void deveExtrairTokenCorretamenteDoHeader() throws Exception {
        // Given
        String token = "extracted.jwt.token";
        String login = "teste_usuario";

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(serviceToken.getToken(token)).thenReturn(login);
        when(serviceAutenticacao.loadUserByUsername(login)).thenReturn(usuario);

        // When
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Then
        verify(serviceToken).getToken(token); // Deve passar apenas o token, sem "Bearer "
    }

    @Test
    void deveContinuarProcessamentoMesmoComErro() throws Exception {
        // Given - Simular erro no filterChain
        String token = "valid.jwt.token";
        String login = "teste_usuario";

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(serviceToken.getToken(token)).thenReturn(login);
        when(serviceAutenticacao.loadUserByUsername(login)).thenReturn(usuario);
        doThrow(new RuntimeException("Erro no filter chain")).when(filterChain).doFilter(request, response);

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);
        });

        // Mesmo com erro, a autenticação deve ter sido definida
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void deveDefinirAutenticacaoComAuthoritiesCorretas() throws Exception {
        // Given
        String token = "valid.jwt.token";
        String login = "teste_usuario";

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(serviceToken.getToken(token)).thenReturn(login);
        when(serviceAutenticacao.loadUserByUsername(login)).thenReturn(usuario);

        // When
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Then
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(authentication);
        assertEquals(login, authentication.getName());
        assertNotNull(authentication.getAuthorities());
        assertTrue(authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_CLIENTE")));
    }
}
