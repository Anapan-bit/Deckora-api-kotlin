package com.deckora.Dekora.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.deckora.Dekora.model.Carpeta;
import com.deckora.Dekora.repository.CarpetaRepository;
import com.deckora.Dekora.repository.CartaRepository;
import com.deckora.Dekora.repository.UsuarioRepository;

import jakarta.transaction.Transactional;




@Service
@Transactional
public class CarpetaService {

    @Autowired
    private CarpetaRepository carpetaRepo;
    
    @Autowired
    private CartaRepository cartaRepo;

    @Autowired
    private UsuarioRepository usuarioRepo;

    public List<Carpeta> findAll(){
        return carpetaRepo.findAll();
    }

    public Carpeta findById(Long id){
        return carpetaRepo.findById(id).get();
    }

    public Carpeta save(Carpeta carpeta){
        return carpetaRepo.save(carpeta);
    }

    public Carpeta patchCarpeta(Long id, Carpeta parcialCarpeta){
        Optional<Carpeta> carpetaOpcional = carpetaRepo.findById(id);
        if (carpetaOpcional.isPresent()) {
            
            Carpeta carpetaActualizar = carpetaOpcional.get();
            
            if (parcialCarpeta.getNombre_carpeta() != null) {
                carpetaActualizar.setNombre_carpeta(parcialCarpeta.getNombre_carpeta());
            }
            return carpetaRepo.save(carpetaActualizar);
        } else {
            return null;
        }

    }

    //Delete (arreglar)
/*         public void delete(Long id) {

        Carpeta carpeta = carpetaRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("Carpeta no encontrada"));

        for (Carta carta : carpeta.getCartas()) {
            carta.getCarpetas().remove(carpeta);
            cartaRepo.save(carta);
        }

        Usuario usuario = carpeta.getUsuario();
        if (usuario != null) {
            usuario.getCarpetas().remove(carpeta);
            usuarioRepo.save(usuario);
        }
        
        carpetaRepo.delete(carpeta);
    } */


}
