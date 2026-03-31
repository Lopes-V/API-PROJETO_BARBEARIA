package com.inicio.back_end.dto;


import jakarta.validation.constraints.NotNull;

public record DTOUsuario (
        @NotNull
        String login,
        @NotNull
        String senha
)
{}
