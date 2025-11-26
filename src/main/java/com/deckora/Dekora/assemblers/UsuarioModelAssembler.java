package com.deckora.Dekora.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.deckora.Dekora.controller.UsuarioController;
import com.deckora.Dekora.model.Usuario;
//Hypermedia as the Engine of Application State (HATEOAS)
//Principio de arquitectura REST que describe como guiar al cliente a traves de los recursos de la API
@Component
public class UsuarioModelAssembler implements RepresentationModelAssembler<Usuario, EntityModel<Usuario>>{
    @SuppressWarnings("null")
    @Override
    public EntityModel<Usuario> toModel(Usuario usuario){
        return EntityModel.of(usuario,
            linkTo(methodOn(UsuarioController.class).getUsuarioById(Long.valueOf(usuario.getId()))).withSelfRel(),
            linkTo(methodOn(UsuarioController.class).getAllUsuarios()).withRel("usuarios"),
            linkTo(methodOn(UsuarioController.class).createUsuario(usuario)).withRel("crear_usuario"),
            linkTo(methodOn(UsuarioController.class).updateUsuario(Long.valueOf(usuario.getId()),usuario)).withRel("actualizar_usuario"),
            linkTo(methodOn(UsuarioController.class).patchUsuario(Long.valueOf(usuario.getId()), usuario)).withRel("patch_usuario"),
            linkTo(methodOn(UsuarioController.class).deleteUsuario(Long.valueOf(usuario.getId()))).withRel("eliminar_usuario")
            );
    }
}