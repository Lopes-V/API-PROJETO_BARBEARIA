package com.inicio.back_end.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.inicio.back_end.dto.DTOUsuario;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class ServiceToken {
    private final String senha = "senha";

    public String gerarToken(DTOUsuario dtoUsuario) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(senha);
            return JWT.create()
                    .withIssuer("API-BARBEARIA") //QUEM EMITIU O TOKEN
                    .withSubject(dtoUsuario.login()) //DE QUEM É O TOKEN
                    .withExpiresAt(dataExpiracao()) //DATA DE EXPIRAÇÃO
                    .sign(algorithm); //MODO DE ALGORITMO
        } catch (JWTCreationException e) {
            throw new RuntimeException("Erro ao tentar criar o token JWT " + e);
        }
    }

    public String getToken(String tokenJWT) {
        try {
            Algorithm algoritmo = Algorithm.HMAC256(senha);
            return JWT.require(algoritmo)
                    .withIssuer("API-BARBEARIA")
                    .build()
                    .verify(tokenJWT)
                    .getSubject();
        } catch (JWTVerificationException e) {
            throw new RuntimeException("Token invalido ou expirado!");
        }
    }

    public Instant dataExpiracao() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}
