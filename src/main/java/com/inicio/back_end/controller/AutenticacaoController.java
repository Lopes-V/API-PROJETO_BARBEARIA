package com.inicio.back_end.controller;

import com.inicio.back_end.dto.DTOToken;
import com.inicio.back_end.dto.DTOUsuario;
import com.inicio.back_end.model.Usuario;
import com.inicio.back_end.service.ServiceToken;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AutenticacaoController {
    private final AuthenticationManager manager;

    private final ServiceToken token;

    public AutenticacaoController(AuthenticationManager manager, ServiceToken token) {
        this.manager = manager;
        this.token = token;
    }

    @PostMapping("/login")
    public ResponseEntity<?> efetuaLogin(@Valid @RequestBody DTOUsuario dtoUsuario){
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(dtoUsuario.login(),dtoUsuario.senha());
        Authentication authentication = manager.authenticate(authenticationToken);
        Usuario usuario = (Usuario) authentication.getPrincipal();
        var tokenJWT = token.gerarToken(dtoUsuario);
        return ResponseEntity.ok(new DTOToken(tokenJWT));
    }

}
