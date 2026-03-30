package com.inicio.back_end.dto.especifico;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record DTOPatchEstoque (
    Long id,
    int quantidade
)
{}