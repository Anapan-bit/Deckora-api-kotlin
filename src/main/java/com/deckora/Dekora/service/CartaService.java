package com.deckora.Dekora.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.deckora.Dekora.model.Carpeta;
import com.deckora.Dekora.model.Carta;
import com.deckora.Dekora.model.Categoria;
import com.deckora.Dekora.model.Usuario;
import com.deckora.Dekora.repository.CarpetaRepository;
import com.deckora.Dekora.repository.CartaRepository;
import com.deckora.Dekora.repository.CategoriaRepository;
import com.deckora.Dekora.repository.UsuarioRepository;

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


    public void delete(Long id) {

        Carta carta = cartaRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("Carta no encontrada"));

        carta.setCategoria(null);

        for (Carpeta carpeta : carta.getCarpetas()) {
            carpeta.getCartas().remove(carta);
            carpetaRepo.save(carpeta);
        }

        carta.getCarpetas().clear();

        cartaRepo.delete(carta);
    }

    //Patch
    public Carta patchCarta(Long id, Carta parcialCarta){
        Optional<Carta> CartaOpcional = cartaRepo.findById(id);

        if (CartaOpcional.isPresent()) {

            Carta cartaActualizar = CartaOpcional.get();

            if (parcialCarta.getNombre_carta() != null) {
                cartaActualizar.setNombre_carta(parcialCarta.getNombre_carta());
            }

            if (parcialCarta.getEstado() != null) {
                cartaActualizar.setEstado(parcialCarta.getEstado());
            }

            if (parcialCarta.getDescripcion() != null) {
                cartaActualizar.setDescripcion(parcialCarta.getDescripcion());
            }

            if (parcialCarta.getImagen_url() != null) {
                cartaActualizar.setImagen_url(parcialCarta.getImagen_url());
            }

            // CAMBIAR CATEGORIA
            if (parcialCarta.getCategoria() != null && parcialCarta.getCategoria().getId() != null) {
                Categoria nuevaCategoria = categoriaRepo.findById(parcialCarta.getCategoria().getId())
                    .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
                cartaActualizar.setCategoria(nuevaCategoria);
            }

            // CAMBIAR CARPETAS
            if (parcialCarta.getCarpetas() != null) {

                // remover relaciones actuales
                for (Carpeta carpeta : cartaActualizar.getCarpetas()) {
                    carpeta.getCartas().remove(cartaActualizar);
                    carpetaRepo.save(carpeta);
                }
                cartaActualizar.getCarpetas().clear();

                // agregar relaciones nuevas
                for (Carpeta carpetaParcial : parcialCarta.getCarpetas()) {
                    Carpeta carpetaReal = carpetaRepo.findById(carpetaParcial.getId())
                        .orElseThrow(() -> new RuntimeException("Carpeta no encontrada"));

                    cartaActualizar.getCarpetas().add(carpetaReal);
                    carpetaReal.getCartas().add(cartaActualizar);
                    carpetaRepo.save(carpetaReal);
                }
            }

            return cartaRepo.save(cartaActualizar);
        } else {
            return null;
        }
    }


}
