package com.inicio.back_end.service;

import com.inicio.back_end.dto.DTOUsuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para ServiceToken
 * Testa geração e validação de tokens JWT
 */
@ExtendWith(MockitoExtension.class)
class ServiceTokenTest {

    @InjectMocks
    private ServiceToken serviceToken;

    @Mock
    private DTOUsuario dtoUsuario;

    @BeforeEach
    void setUp() {
        // Configurar secret via ReflectionTestUtils
        ReflectionTestUtils.setField(serviceToken, "secret", "test-secret-key-for-jwt");
        ReflectionTestUtils.setField(serviceToken, "issuer", "API-BARBEARIA-TEST");

        // Configurar mock do DTOUsuario
        lenient().when(dtoUsuario.login()).thenReturn("teste_usuario");
    }

    @Test
    void deveGerarTokenComSucesso() {
        // When
        String token = serviceToken.gerarToken(dtoUsuario);

        // Then
        assertNotNull(token);
        assertTrue(token.startsWith("eyJ")); // JWT sempre começa com "eyJ"
        assertTrue(token.length() > 100); // Token deve ser longo
    }

    @Test
    void deveValidarTokenComSucesso() {
        // When
        String token = serviceToken.gerarToken(dtoUsuario);
        String subject = serviceToken.getToken(token);

        // Then
        assertEquals("teste_usuario", subject);
    }

    @Test
    void deveLancarExcecaoAoGerarTokenComErro() {
        // Given - Configurar secret inválido
        ReflectionTestUtils.setField(serviceToken, "secret", "");

        // When & Then - JWT com secret vazia ainda funciona, então vamos testar com null
        ReflectionTestUtils.setField(serviceToken, "secret", null);
        
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            serviceToken.gerarToken(dtoUsuario);
        });

        assertTrue(exception.getMessage().contains("Erro ao tentar criar o token JWT"));
    }

    @Test
    void deveRetornarDataExpiracaoCorreta() {
        // When
        var dataExpiracao = serviceToken.dataExpiracao();

        // Then
        assertNotNull(dataExpiracao);
        // Verificar se é 2 horas no futuro (aproximadamente)
        var agora = java.time.LocalDateTime.now().toInstant(java.time.ZoneOffset.of("-03:00"));
        var diferenca = java.time.Duration.between(agora, dataExpiracao);
        assertTrue(diferenca.toHours() >= 1 && diferenca.toHours() <= 3); // Entre 1 e 3 horas
    }
}
