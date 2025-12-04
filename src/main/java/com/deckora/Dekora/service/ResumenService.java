package com.deckora.Dekora.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.deckora.Dekora.model.Resumen;
import com.deckora.Dekora.model.Usuario;
import com.deckora.Dekora.model.Carta;
import com.deckora.Dekora.model.Carpeta;
import com.deckora.Dekora.repository.CartaRepository;
import com.deckora.Dekora.repository.CarpetaRepository;
import com.deckora.Dekora.repository.UsuarioRepository;
import com.deckora.Dekora.repository.ResumenRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ResumenService {

    @Autowired
    private ResumenRepository resumenRepo;

    @Autowired
    private CartaRepository cartaRepo;

    @Autowired
    private CarpetaRepository carpetaRepo;

    @Autowired
    private UsuarioRepository usuarioRepo;

    public List<Resumen> findAll() {
        return resumenRepo.findAll();
    }

    public Resumen findById(Long id) {
        return resumenRepo.findById(id).get();
    }

    public Resumen save(Resumen resumen) {
        return resumenRepo.save(resumen);
    }

    // Delete usuario
    public void borrarUsuario(Long idResumen) {
        Resumen resumen = resumenRepo.findById(idResumen)
                .orElseThrow(() -> new RuntimeException("Resumen no encontrado"));

        Usuario usuario = resumen.getUsuario();
        if (usuario == null) {
            throw new RuntimeException("El resumen no tiene usuario asociado");
        }

        // 1. Obtener todos los resúmenes del usuario
        List<Resumen> resumenesDelUsuario = resumenRepo.findByUsuario(usuario);

        // 2. Eliminar todos los resúmenes relacionados
        resumenRepo.deleteAll(resumenesDelUsuario);

        // 3. Forzar UPDATEs/DELETEs antes de eliminar usuario
        resumenRepo.flush();

        // 4. Eliminar el usuario
        usuarioRepo.delete(usuario);
    }

    // Delete carta
    public void borrarCarta(Long idResumen) {
        Resumen resumen = resumenRepo.findById(idResumen)
                .orElseThrow(() -> new RuntimeException("Resumen no encontrado"));

        Carta carta = resumen.getCarta();
        if (carta == null) {
            throw new RuntimeException("El resumen no tiene carta asociada");
        }

        // Borrar todas las conexiones de esa carta
        List<Resumen> resumenes = resumenRepo.findByCarta(carta);
        resumenRepo.deleteAll(resumenes);
        resumenRepo.flush();

        // Borrar la carta
        cartaRepo.delete(carta);
    }

    // Delete carpeta
    public void borrarCarpeta(Long idResumen) {
        Resumen resumen = resumenRepo.findById(idResumen)
                .orElseThrow(() -> new RuntimeException("Resumen no encontrado"));

        Carpeta carpeta = resumen.getCarpeta();
        if (carpeta == null) {
            throw new RuntimeException("El resumen no tiene carpeta asociada");
        }

        // Borrar todas las conexiones de esa carpeta
        List<Resumen> resumenes = resumenRepo.findByCarpeta(carpeta);
        resumenRepo.deleteAll(resumenes);
        resumenRepo.flush();

        // Borrar la carpeta
        carpetaRepo.delete(carpeta);
    }

    // Mover carta
    public Resumen moverCarta(Long idResumen, Long idNuevaCarpeta) {
        Resumen resumen = resumenRepo.findById(idResumen)
                .orElseThrow(() -> new RuntimeException("Resumen no encontrado"));

        Carpeta nuevaCarpeta = carpetaRepo.findById(idNuevaCarpeta)
                .orElseThrow(() -> new RuntimeException("Carpeta nueva no encontrada"));

        // Actualizar solo la carpeta de este resumen
        resumen.setCarpeta(nuevaCarpeta);

        return resumenRepo.save(resumen);
    }

    // Querys
    // Carpetas de un usuario
    public List<Carpeta> getCarpetasByUsuario(Long idUsuario) {
        return resumenRepo.findCarpetasByUsuario(idUsuario);
    }

    // cartas de una carpeta
    public List<Carta> getCartasByCarpeta(Long idCarpeta) {
        return resumenRepo.findCartasByCarpeta(idCarpeta);
    }

    // detalles usuario
    public Usuario getUsuarioById(Long idUsuario) {
        return usuarioRepo.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

}
