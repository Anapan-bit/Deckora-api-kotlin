package com.deckora.Dekora.repository;
import com.deckora.Dekora.model.Usuario;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;


@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

}
