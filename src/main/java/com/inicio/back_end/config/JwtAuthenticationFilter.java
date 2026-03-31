package com.inicio.back_end.config;

import com.inicio.back_end.service.ServiceAutenticacao;
import com.inicio.back_end.service.ServiceToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filtro JWT que valida tokens nas requisições
 * Executado uma vez por requisição para extrair e validar o token JWT
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final ServiceToken serviceToken;
    private final ServiceAutenticacao serviceAutenticacao;

    public JwtAuthenticationFilter(ServiceToken serviceToken, ServiceAutenticacao serviceAutenticacao) {
        this.serviceToken = serviceToken;
        this.serviceAutenticacao = serviceAutenticacao;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String token = extrairToken(request);
            
            if (token != null) {
                String login = serviceToken.getToken(token);
                UserDetails user = serviceAutenticacao.loadUserByUsername(login);
                
                // Criar autenticação e colocar no contexto de segurança
                UsernamePasswordAuthenticationToken authentication = 
                    new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (RuntimeException e) {
            logger.debug("Erro ao validar token JWT: " + e.getMessage());
            // Token inválido ou expirado - continua sem autenticação
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Extrai o token do header Authorization
     * Esperado formato: "Bearer {token}"
     */
    private String extrairToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }
}

