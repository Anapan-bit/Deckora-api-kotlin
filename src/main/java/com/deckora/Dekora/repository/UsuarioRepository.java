package com.deckora.Dekora.repository;
import com.deckora.Dekora.model.Usuario;

import org.springframework.stereotype.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByNombreUsuario(String nombre_usuario);
    Optional<Usuario> findByNombre_usuario(String nombre_usuario);
}
