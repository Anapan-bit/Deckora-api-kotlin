package com.deckora.Dekora.repository;

import com.deckora.Dekora.model.Carpeta;
import com.deckora.Dekora.model.Carta;
import com.deckora.Dekora.model.Resumen;
import com.deckora.Dekora.model.Usuario;

import org.springframework.stereotype.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

@Repository
public interface ResumenRepository extends JpaRepository<Resumen, Long> {
    List<Resumen> findByUsuario(Usuario usuario);

    List<Resumen> findByCarta(Carta carta);

    List<Resumen> findByCarpeta(Carpeta carpeta);

    // Querys
    // carpetas de un usuario
    @Query("SELECT DISTINCT r.carpeta FROM Resumen r WHERE r.usuario.id = :idUsuario")
    List<Carpeta> findCarpetasByUsuario(Long idUsuario);

    // cartas de una carpeta
    @Query("SELECT DISTINCT r.carta FROM Resumen r WHERE r.carpeta.id = :idCarpeta")
    List<Carta> findCartasByCarpeta(Long idCarpeta);

    // Detalles usuario
    @Query("SELECT r.usuario FROM Resumen r WHERE r.id = :idResumen")
    Usuario findUsuarioByResumen(Long idResumen);

}
