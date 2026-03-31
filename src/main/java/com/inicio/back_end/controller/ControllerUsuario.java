package com.inicio.back_end.controller;

import com.inicio.back_end.dto.DTOUsuario;
import com.inicio.back_end.model.Usuario;
import com.inicio.back_end.service.ServiceUsuario;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/usuarios")
public class ControllerUsuario {

    private final ServiceUsuario serviceUsuario;

    public ControllerUsuario(ServiceUsuario serviceUsuario) {
        this.serviceUsuario = serviceUsuario;
    }

    /**
     * Registra um novo usuário no sistema
     * POST /usuarios/registrar
     */
    @PostMapping("/registrar")
    public ResponseEntity<?> registrarUsuario(@Valid @RequestBody DTOUsuario dtoUsuario) {
        try {
            Usuario usuario = serviceUsuario.registrarUsuario(dtoUsuario);
            URI uri = ServletUriComponentsBuilder
                    .fromCurrentRequestUri()
                    .path("/{id}")
                    .buildAndExpand(usuario.getId())
                    .toUri();
            
            return ResponseEntity.created(uri).body(Map.of(
                    "success", true,
                    "message", "Usuário registrado com sucesso",
                    "data", Map.of(
                            "id", usuario.getId(),
                            "login", usuario.getUsername()
                    )
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "success", false,
                    "message", "Erro ao registrar usuário: " + e.getMessage()
            ));
        }
    }

    /**
     * Retorna os dados do usuário autenticado
     * GET /usuarios/me
     */
    @GetMapping("/me")
    public ResponseEntity<?> getUsuarioAutenticado() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            
            if (authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                        "success", false,
                        "message", "Usuário não autenticado"
                ));
            }

            Usuario usuario = (Usuario) authentication.getPrincipal();
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Dados do usuário recuperados com sucesso",
                    "data", Map.of(
                            "id", usuario.getId(),
                            "login", usuario.getUsername()
                    )
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "success", false,
                    "message", "Erro ao recuperar dados do usuário: " + e.getMessage()
            ));
        }
    }
}


