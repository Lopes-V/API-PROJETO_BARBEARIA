package com.inicio.back_end.service;

import com.inicio.back_end.dto.DTOUsuario;
import com.inicio.back_end.model.Usuario;
import com.inicio.back_end.repository.RepositoryUsuario;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ServiceUsuario {

    private final RepositoryUsuario repositoryUsuario;
    private final PasswordEncoder passwordEncoder;

    public ServiceUsuario(RepositoryUsuario repositoryUsuario, PasswordEncoder passwordEncoder) {
        this.repositoryUsuario = repositoryUsuario;
        this.passwordEncoder = passwordEncoder;
    }

    public Usuario registrarUsuario(DTOUsuario dto) {
        Optional<Usuario> usuarioExistente = repositoryUsuario.findByLogin(dto.login());
        if (usuarioExistente.isPresent()) {
            throw new RuntimeException("Usuário com este login já existe!");
        }
        Usuario usuario = new Usuario();
        usuario.setLogin(dto.login());
        usuario.setSenha(passwordEncoder.encode(dto.senha()));

        return repositoryUsuario.save(usuario);
    }

    public Optional<Usuario> buscarPorLogin(String login) {
        return repositoryUsuario.findByLogin(login);
    }

    public Optional<Usuario> buscarPorId(Long id) {
        return repositoryUsuario.findById(id);
    }
}

