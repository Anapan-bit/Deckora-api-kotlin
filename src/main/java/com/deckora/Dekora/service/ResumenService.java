package com.deckora.Dekora.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.deckora.Dekora.model.Resumen;
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

    public List<Resumen> findAll(){
        return resumenRepo.findAll();
    }

    public Resumen findById(Long id){
        return resumenRepo.findById(id).get();
    }

    public Resumen save(Resumen resumen){
        return resumenRepo.save(resumen);
    }

    //Delete
/*     public void delete(Long id) {

        Categoria categoria = categoriaRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        // Desasociar cartas
        categoria.getCartas().forEach(carta -> carta.setCategoria(null));
        cartaRepo.saveAll(categoria.getCartas());

        // Borrar la categoría
        categoriaRepo.delete(categoria);
    } */
    
    //Patch
/*     public Categoria patchCategoria(Long id, Categoria parcialCategoria){
        Optional<Categoria> categoriaOpcional = categoriaRepo.findById(id);
        if (categoriaOpcional.isPresent()) {
            
            Categoria categoriaActualizar = categoriaOpcional.get();
            
            if (parcialCategoria.getNombre_categoria() != null) {
                categoriaActualizar.setNombre_categoria(parcialCategoria.getNombre_categoria());
            }
            return categoriaRepo.save(categoriaActualizar);
        } else {
            return null;
        }
    } */


}
