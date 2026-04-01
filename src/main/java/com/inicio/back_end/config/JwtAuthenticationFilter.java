package com.inicio.back_end.config;

import com.inicio.back_end.service.ServiceAutenticacao;
import com.inicio.back_end.service.ServiceToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final ServiceToken serviceToken;
    private final ServiceAutenticacao serviceAutenticacao;

    public JwtAuthenticationFilter(ServiceToken serviceToken, ServiceAutenticacao serviceAutenticacao) {
        this.serviceToken = serviceToken;
        this.serviceAutenticacao = serviceAutenticacao;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,@NonNull HttpServletResponse response,@NonNull FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = extrairToken(request);
            
            if (token != null) {
                String login = serviceToken.getToken(token);
                UserDetails user = serviceAutenticacao.loadUserByUsername(login);

                UsernamePasswordAuthenticationToken authentication = 
                    new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (RuntimeException e) {
            logger.debug("Erro ao validar token JWT: " + e.getMessage());
        }

        filterChain.doFilter(request, response);
    }

    private String extrairToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }
}

