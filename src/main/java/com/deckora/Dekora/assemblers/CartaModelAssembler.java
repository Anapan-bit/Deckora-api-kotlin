package com.deckora.Dekora.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.deckora.Dekora.controller.CartaController;
import com.deckora.Dekora.model.Carta;
//Hypermedia as the Engine of Application State (HATEOAS)
//Principio de arquitectura REST que describe como guiar al cliente a traves de los recursos de la API
@Component
public class CartaModelAssembler implements RepresentationModelAssembler<Carta, EntityModel<Carta>>{
    @SuppressWarnings("null")
    @Override
    public EntityModel<Carta> toModel(Carta carta){
        return EntityModel.of(carta,
            linkTo(methodOn(CartaController.class).getCartaById(Long.valueOf(carta.getId()))).withSelfRel(),
            linkTo(methodOn(CartaController.class).getAllCartas()).withRel("cartas"),
            linkTo(methodOn(CartaController.class).updateCarta(Long.valueOf(carta.getId()),carta)).withRel("actualizar_carta")
/*             linkTo(methodOn(CartaController.class).patchCarta(Long.valueOf(carta.getId()), carta)).withRel("patch_carta"),
            linkTo(methodOn(CartaController.class).deleteCarta(Long.valueOf(carta.getId()))).withRel("eliminar_carta") */
            );
    }
}