package com.inicio.back_end.model.enums;

/**
 * Enum para os papéis/roles do sistema
 */
public enum RoleUsuario {
    ADMIN("ADMIN", "Administrador da barbearia - Acesso total"),
    BARBEIRO("BARBEIRO", "Barbeiro - Pode gerenciar seus agendamentos"),
    CLIENTE("CLIENTE", "Cliente - Pode agendar serviços"),
    RECEPCIONISTA("RECEPCIONISTA", "Recepcionista - Gerencia agendamentos e clientes");

    private final String codigo;
    private final String descricao;

    RoleUsuario(String codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getDescricao() {
        return descricao;
    }
}

