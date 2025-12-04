package com.deckora.Dekora.repository;
import com.deckora.Dekora.model.Usuario;

import org.springframework.stereotype.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    @Query("SELECT u FROM Usuario u WHERE u.nombre_usuario = :nombre")
    Optional<Usuario> findByNombre(@Param("nombre") String nombre);

}
