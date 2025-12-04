package com.deckora.Dekora.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.deckora.Dekora.model.Carpeta;
import com.deckora.Dekora.model.Carta;
import com.deckora.Dekora.model.Usuario;
import com.deckora.Dekora.repository.CarpetaRepository;
import com.deckora.Dekora.repository.CartaRepository;
import com.deckora.Dekora.repository.UsuarioRepository;

import jakarta.transaction.Transactional;




@Service
@Transactional
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepo;
/*     
    @Autowired
    private CarpetaRepository carpetaRepo;

    @Autowired
    private CartaRepository cartaRepo; */

    public List<Usuario> findAll(){
        return usuarioRepo.findAll();
    }

    public Usuario findById(Long id){
        return usuarioRepo.findById(id).get();
    }

    public Usuario save(Usuario usuario){
        return usuarioRepo.save(usuario);
    }


    public Usuario patchUsuario(Long id, Usuario parcialUsuario){
        Optional<Usuario> usuarioOpcional = usuarioRepo.findById(id);
        if (usuarioOpcional.isPresent()) {
            
            Usuario usuatioActualizar = usuarioOpcional.get();
            
            if (parcialUsuario.getNombre_usuario() != null) {
                usuatioActualizar.setNombre_usuario(parcialUsuario.getNombre_usuario());
            }

            if (parcialUsuario.getCorreo_usuario() != null) {
                usuatioActualizar.setCorreo_usuario(parcialUsuario.getCorreo_usuario());
            }

            if (parcialUsuario.getContrasenia_usuario() != null) {
                usuatioActualizar.setContrasenia_usuario(parcialUsuario.getContrasenia_usuario());
            }
            
            return usuarioRepo.save(usuatioActualizar);
        } else {
            return null;
        }

    }

    public Usuario login(String nombre_usuario, String contrasenia_usuario) {
        Optional<Usuario> usuarioOpcional = usuarioRepo.findByNombre_usuario(nombre_usuario);

        if (usuarioOpcional.isPresent()) {
            Usuario usuario = usuarioOpcional.get();
            // Verifica la contraseña
            if (usuario.getContrasenia_usuario().equals(contrasenia_usuario)) {
                return usuario;
            }
        }
        return null; // Usuario no encontrado o contraseña incorrecta
    }

    public Optional<Usuario> findByNombre_usuario(String nombre) {
        return usuarioRepo.findByNombre_usuario(nombre);
    }
    // Delete (arreglar)
    
/*     public void delete(Long id) {

        Usuario usuario = usuarioRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        for (Carpeta carpeta : usuario.getCarpetas()) {

            for (Carta carta : carpeta.getCartas()) {
                cartaRepo.delete(carta);
            }

            carpetaRepo.delete(carpeta);
        }
        usuarioRepo.delete(usuario);
    } */

}
