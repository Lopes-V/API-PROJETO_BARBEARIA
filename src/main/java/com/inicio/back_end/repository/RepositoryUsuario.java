package com.inicio.back_end.repository;

import com.inicio.back_end.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface RepositoryUsuario extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByLogin(String username);
}
