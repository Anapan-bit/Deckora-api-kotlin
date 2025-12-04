package com.deckora.Dekora.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.deckora.Dekora.controller.ResumenController;
import com.deckora.Dekora.model.Resumen;
//Hypermedia as the Engine of Application State (HATEOAS)
//Principio de arquitectura REST que describe como guiar al cliente a traves de los recursos de la API
@Component
public class ResumenModelAssembler implements RepresentationModelAssembler<Resumen, EntityModel<Resumen>>{
    @SuppressWarnings("null")
    @Override
    public EntityModel<Resumen> toModel(Resumen resumen){
        return EntityModel.of(resumen,
            linkTo(methodOn(ResumenController.class).getResumenById(Long.valueOf(resumen.getId()))).withSelfRel(),
            linkTo(methodOn(ResumenController.class).getAllResumenes()).withRel("resumens"),
            linkTo(methodOn(ResumenController.class).createResumen(resumen)).withRel("crear_resumen"),
            linkTo(methodOn(ResumenController.class).updateResumen(Long.valueOf(resumen.getId()),resumen)).withRel("actualizar_resumen")
            );
    }
}