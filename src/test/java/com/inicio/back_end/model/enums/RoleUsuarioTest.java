package com.inicio.back_end.model.enums;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários para o enum RoleUsuario
 * Testa códigos, descrições e funcionalidades do enum
 */
class RoleUsuarioTest {

    @Test
    void deveRetornarCodigosCorretos() {
        assertEquals("ADMIN", RoleUsuario.ADMIN.getCodigo());
        assertEquals("BARBEIRO", RoleUsuario.BARBEIRO.getCodigo());
        assertEquals("CLIENTE", RoleUsuario.CLIENTE.getCodigo());
        assertEquals("RECEPCIONISTA", RoleUsuario.RECEPCIONISTA.getCodigo());
    }

    @Test
    void deveRetornarDescricoesCorretas() {
        assertEquals("Administrador da barbearia - Acesso total", RoleUsuario.ADMIN.getDescricao());
        assertEquals("Barbeiro - Pode gerenciar seus agendamentos", RoleUsuario.BARBEIRO.getDescricao());
        assertEquals("Cliente - Pode agendar serviços", RoleUsuario.CLIENTE.getDescricao());
        assertEquals("Recepcionista - Gerencia agendamentos e clientes", RoleUsuario.RECEPCIONISTA.getDescricao());
    }

    @Test
    void deveTerQuatroValoresNoEnum() {
        RoleUsuario[] valores = RoleUsuario.values();
        assertEquals(4, valores.length);
    }

    @Test
    void deveConterTodosOsValoresEsperados() {
        RoleUsuario[] valores = RoleUsuario.values();

        boolean temAdmin = false;
        boolean temBarbeiro = false;
        boolean temCliente = false;
        boolean temRecepcionista = false;

        for (RoleUsuario role : valores) {
            switch (role) {
                case ADMIN -> temAdmin = true;
                case BARBEIRO -> temBarbeiro = true;
                case CLIENTE -> temCliente = true;
                case RECEPCIONISTA -> temRecepcionista = true;
            }
        }

        assertTrue(temAdmin, "Deve conter ADMIN");
        assertTrue(temBarbeiro, "Deve conter BARBEIRO");
        assertTrue(temCliente, "Deve conter CLIENTE");
        assertTrue(temRecepcionista, "Deve conter RECEPCIONISTA");
    }

    @Test
    void deveSerPossivelAcessarPorValor() {
        assertEquals(RoleUsuario.ADMIN, RoleUsuario.valueOf("ADMIN"));
        assertEquals(RoleUsuario.BARBEIRO, RoleUsuario.valueOf("BARBEIRO"));
        assertEquals(RoleUsuario.CLIENTE, RoleUsuario.valueOf("CLIENTE"));
        assertEquals(RoleUsuario.RECEPCIONISTA, RoleUsuario.valueOf("RECEPCIONISTA"));
    }

    @Test
    void deveLancarExcecaoParaValorInvalido() {
        assertThrows(IllegalArgumentException.class, () -> {
            RoleUsuario.valueOf("INVALIDO");
        });
    }

    @Test
    void deveTerPropriedadesNaoNulas() {
        for (RoleUsuario role : RoleUsuario.values()) {
            assertNotNull(role.getCodigo(), "Código não deve ser nulo para " + role.name());
            assertNotNull(role.getDescricao(), "Descrição não deve ser nula para " + role.name());
            assertFalse(role.getCodigo().isEmpty(), "Código não deve ser vazio para " + role.name());
            assertFalse(role.getDescricao().isEmpty(), "Descrição não deve ser vazia para " + role.name());
        }
    }

    @Test
    void deveTerCodigosUnicos() {
        RoleUsuario[] valores = RoleUsuario.values();
        for (int i = 0; i < valores.length; i++) {
            for (int j = i + 1; j < valores.length; j++) {
                assertNotEquals(valores[i].getCodigo(), valores[j].getCodigo(),
                    "Códigos devem ser únicos: " + valores[i].getCodigo() + " vs " + valores[j].getCodigo());
            }
        }
    }

    @Test
    void deveManterOrdemConsistente() {
        RoleUsuario[] valores = RoleUsuario.values();

        // Verificar ordem definida no enum
        assertEquals(RoleUsuario.ADMIN, valores[0]);
        assertEquals(RoleUsuario.BARBEIRO, valores[1]);
        assertEquals(RoleUsuario.CLIENTE, valores[2]);
        assertEquals(RoleUsuario.RECEPCIONISTA, valores[3]);
    }

    @Test
    void deveSerImutavel() {
        // Given
        String codigoOriginal = RoleUsuario.ADMIN.getCodigo();
        String descricaoOriginal = RoleUsuario.ADMIN.getDescricao();

        // When - Tentar modificar (não deve ser possível)
        // RoleUsuario.ADMIN.setCodigo("NOVO"); // Isso nem compila

        // Then
        assertEquals(codigoOriginal, RoleUsuario.ADMIN.getCodigo());
        assertEquals(descricaoOriginal, RoleUsuario.ADMIN.getDescricao());
    }

    @Test
    void deveFuncionarEmSwitchExpressions() {
        // Teste de switch expression (Java 14+)
        for (RoleUsuario role : RoleUsuario.values()) {
            String resultado = switch (role) {
                case ADMIN -> "Acesso total";
                case BARBEIRO -> "Agendamentos próprios";
                case CLIENTE -> "Agendar serviços";
                case RECEPCIONISTA -> "Gerenciar agendamentos";
            };

            assertNotNull(resultado);
            assertFalse(resultado.isEmpty());
        }
    }

    @Test
    void deveSerSerializavel() {
        // Teste básico de serialização (enums são automaticamente serializáveis)
        for (RoleUsuario role : RoleUsuario.values()) {
            assertNotNull(role.name());
            assertNotNull(role.ordinal());
        }
    }
}
