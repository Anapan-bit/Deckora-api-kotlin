package com.deckora.Dekora.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.deckora.Dekora.controller.CategoriaController;
import com.deckora.Dekora.model.Categoria;
//Hypermedia as the Engine of Application State (HATEOAS)
//Principio de arquitectura REST que describe como guiar al cliente a traves de los recursos de la API
@Component
public class CategoriaModelAssembler implements RepresentationModelAssembler<Categoria, EntityModel<Categoria>>{
    @SuppressWarnings("null")
    @Override
    public EntityModel<Categoria> toModel(Categoria categoria){
        return EntityModel.of(categoria,
            linkTo(methodOn(CategoriaController.class).getCategoriaById(Long.valueOf(categoria.getId()))).withSelfRel(),
            linkTo(methodOn(CategoriaController.class).getAllCategorias()).withRel("categorias"),
            linkTo(methodOn(CategoriaController.class).createCategoria(categoria)).withRel("crear_categoria"),
            linkTo(methodOn(CategoriaController.class).updateCategoria(Long.valueOf(categoria.getId()),categoria)).withRel("actualizar_categoria"),
            linkTo(methodOn(CategoriaController.class).patchCategoria(Long.valueOf(categoria.getId()), categoria)).withRel("patch_categoria"),
            linkTo(methodOn(CategoriaController.class).deleteCategoria(Long.valueOf(categoria.getId()))).withRel("eliminar_categoria")
            );
    }
}