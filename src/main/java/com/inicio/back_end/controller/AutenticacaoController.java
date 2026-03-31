package com.inicio.back_end.controller;

import com.inicio.back_end.dto.DTOToken;
import com.inicio.back_end.dto.DTOUsuario;
import com.inicio.back_end.model.Usuario;
import com.inicio.back_end.service.ServiceToken;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AutenticacaoController {
    
    private final AuthenticationManager manager;
    private final ServiceToken token;

    public AutenticacaoController(AuthenticationManager manager, ServiceToken token) {
        this.manager = manager;
        this.token = token;
    }

    /**
     * Realiza login do usuário e retorna um token JWT
     * POST /auth/login
     */
    @PostMapping("/login")
    public ResponseEntity<?> efetuaLogin(@Valid @RequestBody DTOUsuario dtoUsuario) {
        try {
            UsernamePasswordAuthenticationToken authenticationToken = 
                new UsernamePasswordAuthenticationToken(dtoUsuario.login(), dtoUsuario.senha());
            
            Authentication authentication = manager.authenticate(authenticationToken);
            Usuario usuario = (Usuario) authentication.getPrincipal();
            
            String tokenJWT = token.gerarToken(dtoUsuario);
            
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Login realizado com sucesso",
                    "data", Map.of(
                            "token", tokenJWT,
                            "usuario", Map.of(
                                    "id", usuario.getId(),
                                    "login", usuario.getUsername()
                            )
                    )
            ));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401).body(Map.of(
                    "success", false,
                    "message", "Usuário ou senha incorretos"
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body(Map.of(
                    "success", false,
                    "message", "Erro ao processar login: " + e.getMessage()
            ));
        }
    }
}


