package com.inicio.back_end.service;

import com.inicio.back_end.dto.DTOUsuario;
import com.inicio.back_end.model.Usuario;
import com.inicio.back_end.repository.RepositoryUsuario;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Serviço para gerenciar operações de usuários
 */
@Service
public class ServiceUsuario {

    private final RepositoryUsuario repositoryUsuario;
    private final PasswordEncoder passwordEncoder;

    public ServiceUsuario(RepositoryUsuario repositoryUsuario, PasswordEncoder passwordEncoder) {
        this.repositoryUsuario = repositoryUsuario;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Registra um novo usuário com senha criptografada
     */
    public Usuario registrarUsuario(DTOUsuario dto) {
        // Validar se o usuário já existe
        Optional<Usuario> usuarioExistente = repositoryUsuario.findByLogin(dto.login());
        if (usuarioExistente.isPresent()) {
            throw new RuntimeException("Usuário com este login já existe!");
        }

        // Criar novo usuário com senha criptografada
        Usuario usuario = new Usuario();
        usuario.setLogin(dto.login());
        usuario.setSenha(passwordEncoder.encode(dto.senha()));

        return repositoryUsuario.save(usuario);
    }

    /**
     * Busca um usuário pelo login
     */
    public Optional<Usuario> buscarPorLogin(String login) {
        return repositoryUsuario.findByLogin(login);
    }

    /**
     * Busca um usuário pelo ID
     */
    public Optional<Usuario> buscarPorId(Long id) {
        return repositoryUsuario.findById(id);
    }
}

