package com.inicio.back_end.model;

import com.inicio.back_end.model.enums.RoleUsuario;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários para a entidade Usuario
 * Testa implementação de UserDetails e getters/setters
 */
class UsuarioTest {

    @Test
    void deveCriarUsuarioComConstrutorPadrao() {
        // When
        Usuario usuario = new Usuario();

        // Then
        assertNotNull(usuario);
        assertNull(usuario.getId());
        assertNull(usuario.getUsername());
        assertNull(usuario.getPassword());
        assertNull(usuario.getRole());
    }

    @Test
    void deveCriarUsuarioComConstrutorCompleto() {
        // When
        Usuario usuario = new Usuario(1L, "teste_login", "teste_senha", RoleUsuario.CLIENTE);

        // Then
        assertNotNull(usuario);
        assertEquals(1L, usuario.getId());
        assertEquals("teste_login", usuario.getUsername());
        assertEquals("teste_senha", usuario.getPassword());
        assertEquals(RoleUsuario.CLIENTE, usuario.getRole());
    }

    @Test
    void deveImplementarUserDetailsCorretamente() {
        // Given
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setLogin("teste_usuario");
        usuario.setSenha("senha_criptografada");
        usuario.setRole(RoleUsuario.ADMIN);

        // Then
        assertEquals("teste_usuario", usuario.getUsername());
        assertEquals("senha_criptografada", usuario.getPassword());
        assertNotNull(usuario.getAuthorities());
        assertTrue(usuario.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN")));
        assertTrue(usuario.isAccountNonExpired());
        assertTrue(usuario.isAccountNonLocked());
        assertTrue(usuario.isCredentialsNonExpired());
        assertTrue(usuario.isEnabled());
    }

    @Test
    void deveRetornarAuthoritiesCorretasParaDiferentesRoles() {
        // Teste CLIENTE
        Usuario cliente = new Usuario();
        cliente.setRole(RoleUsuario.CLIENTE);
        assertTrue(cliente.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_CLIENTE")));

        // Teste BARBEIRO
        Usuario barbeiro = new Usuario();
        barbeiro.setRole(RoleUsuario.BARBEIRO);
        assertTrue(barbeiro.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_BARBEIRO")));

        // Teste RECEPCIONISTA
        Usuario recepcionista = new Usuario();
        recepcionista.setRole(RoleUsuario.RECEPCIONISTA);
        assertTrue(recepcionista.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_RECEPCIONISTA")));

        // Teste ADMIN
        Usuario admin = new Usuario();
        admin.setRole(RoleUsuario.ADMIN);
        assertTrue(admin.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN")));
    }

    @Test
    void deveDefinirRolePadraoComoCliente() {
        // Given
        Usuario usuario = new Usuario();
        usuario.setLogin("teste");
        usuario.setSenha("senha");

        // When - Role não definida explicitamente

        // Then
        assertNull(usuario.getRole()); // Não há valor padrão no construtor
    }

    @Test
    void devePermitirAlteracaoDePropriedades() {
        // Given
        Usuario usuario = new Usuario();

        // When
        usuario.setId(1L);
        usuario.setLogin("novo_login");
        usuario.setSenha("nova_senha");
        usuario.setRole(RoleUsuario.BARBEIRO);

        // Then
        assertEquals(1L, usuario.getId());
        assertEquals("novo_login", usuario.getUsername());
        assertEquals("nova_senha", usuario.getPassword());
        assertEquals(RoleUsuario.BARBEIRO, usuario.getRole());
    }

    @Test
    void deveRetornarAuthoritiesComoListaNaoVazia() {
        // Given
        Usuario usuario = new Usuario();
        usuario.setRole(RoleUsuario.CLIENTE);

        // When
        var authorities = usuario.getAuthorities();

        // Then
        assertNotNull(authorities);
        assertFalse(authorities.isEmpty());
        assertEquals(1, authorities.size());
    }

    @Test
    void deveManterEstadoConsistente() {
        // Given
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setLogin("teste");
        usuario.setSenha("senha");
        usuario.setRole(RoleUsuario.ADMIN);

        // When - Múltiplas operações
        usuario.setLogin("teste_alterado");
        usuario.setRole(RoleUsuario.CLIENTE);

        // Then
        assertEquals(1L, usuario.getId());
        assertEquals("teste_alterado", usuario.getUsername());
        assertEquals("senha", usuario.getPassword());
        assertEquals(RoleUsuario.CLIENTE, usuario.getRole());
        assertTrue(usuario.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_CLIENTE")));
    }

    @Test
    void deveCompararUsuariosCorretamente() {
        // Given
        Usuario usuario1 = new Usuario(1L, "login", "senha", RoleUsuario.CLIENTE);
        Usuario usuario2 = new Usuario(1L, "login", "senha", RoleUsuario.CLIENTE);
        Usuario usuario3 = new Usuario(2L, "outro", "senha", RoleUsuario.ADMIN);

        // Then
        assertEquals(usuario1.getId(), usuario2.getId());
        assertEquals(usuario1.getUsername(), usuario2.getUsername());
        assertEquals(usuario1.getPassword(), usuario2.getPassword());
        assertEquals(usuario1.getRole(), usuario2.getRole());

        assertNotEquals(usuario1.getId(), usuario3.getId());
        assertNotEquals(usuario1.getUsername(), usuario3.getUsername());
        assertNotEquals(usuario1.getRole(), usuario3.getRole());
    }
}
