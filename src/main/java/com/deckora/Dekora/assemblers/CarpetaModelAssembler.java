package com.deckora.Dekora.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.deckora.Dekora.controller.CarpetaController;
import com.deckora.Dekora.model.Carpeta;
//Hypermedia as the Engine of Application State (HATEOAS)
//Principio de arquitectura REST que describe como guiar al cliente a traves de los recursos de la API
@Component
public class CarpetaModelAssembler implements RepresentationModelAssembler<Carpeta, EntityModel<Carpeta>>{
    @SuppressWarnings("null")
    @Override
    public EntityModel<Carpeta> toModel(Carpeta carpeta){
        return EntityModel.of(carpeta,
            linkTo(methodOn(CarpetaController.class).getCarpetaById(Long.valueOf(carpeta.getId()))).withSelfRel(),
            linkTo(methodOn(CarpetaController.class).getAllCarpetas()).withRel("carpetas"),
            linkTo(methodOn(CarpetaController.class).createCarpeta(carpeta)).withRel("crear_carpeta"),
            linkTo(methodOn(CarpetaController.class).updateCarpeta(Long.valueOf(carpeta.getId()),carpeta)).withRel("actualizar_carpeta"),
            linkTo(methodOn(CarpetaController.class).patchCarpeta(Long.valueOf(carpeta.getId()), carpeta)).withRel("patch_carpeta"),
            linkTo(methodOn(CarpetaController.class).deleteCarpeta(Long.valueOf(carpeta.getId()))).withRel("eliminar_carpeta")
            );
    }
}