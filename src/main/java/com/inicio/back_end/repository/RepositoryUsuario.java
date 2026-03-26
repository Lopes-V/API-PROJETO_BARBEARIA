package com.inicio.back_end.repository;

import com.inicio.back_end.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RepositoryUsuario extends JpaRepository<Usuario, UUID> {
}
