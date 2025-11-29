package com.deckora.Dekora.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.deckora.Dekora.model.Carpeta;
import com.deckora.Dekora.model.Carta;
import com.deckora.Dekora.model.Categoria;
import com.deckora.Dekora.repository.CarpetaRepository;
import com.deckora.Dekora.repository.CartaRepository;
import com.deckora.Dekora.repository.CategoriaRepository;

import jakarta.transaction.Transactional;




@Service
@Transactional
public class CartaService {

    @Autowired
    private CartaRepository cartaRepo;

    @Autowired
    private CategoriaRepository categoriaRepo;
    
    @Autowired
    private CarpetaRepository carpetaRepo;


    public List<Carta> findAll(){
        return cartaRepo.findAll();
    }

    public Carta findById(Long id){
        return cartaRepo.findById(id).get();
    }

    public Carta save(Carta carta){
        return cartaRepo.save(carta);
    }


    //Patch (hacer xd)

    //Delete(arreglar)
/*         public void delete(Long id) {

        Carta carta = cartaRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("Carta no encontrada"));

        carta.setCategoria(null);

        for (Carpeta carpeta : carta.getCarpetas()) {
            carpeta.getCartas().remove(carta);
            carpetaRepo.save(carpeta);
        }

        carta.getCarpetas().clear();

        cartaRepo.delete(carta);
    } */
   

}
