package com.deckora.Dekora.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.deckora.Dekora.model.Carta;
import com.deckora.Dekora.model.Categoria;
import com.deckora.Dekora.repository.CartaRepository;
import com.deckora.Dekora.repository.CategoriaRepository;

import jakarta.transaction.Transactional;




@Service
@Transactional
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepo;
    
    @Autowired
    private CartaRepository cartaRepo;

    public List<Categoria> findAll(){
        return categoriaRepo.findAll();
    }

    public Categoria findById(Long id){
        return categoriaRepo.findById(id).get();
    }

    public Categoria save(Categoria categoria){
        return categoriaRepo.save(categoria);
    }

    public void delete(Long id) {

        Categoria categoria = categoriaRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        // Desasociar cartas
        categoria.getCartas().forEach(carta -> carta.setCategoria(null));
        cartaRepo.saveAll(categoria.getCartas());

        // Borrar la categoría
        categoriaRepo.delete(categoria);
    }
    
    public Categoria patchCategoria(Long id, Categoria parcialCategoria){
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
    }


}
