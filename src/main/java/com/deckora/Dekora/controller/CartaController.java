package com.deckora.Dekora.controller;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deckora.Dekora.assemblers.CartaModelAssembler;
import com.deckora.Dekora.model.Carta;
import com.deckora.Dekora.service.CartaService;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@RequestMapping("/api/v2/cartas")
//tag es para dar el titulo en el swagger de la cartas
@Tag(name = "Cartas", description = "Operaciones relacionadas con la creacion, modificación y eliminación de las cartas.")
public class CartaController {

    @Autowired
    private CartaService cartaService;

    @Autowired
    private CartaModelAssembler cartaAssembler;

    //get de todos las cartas
    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    //operation es para el resumen de lo que hace
    //paramater es para describir el tipo de dato esperado
    @Operation(summary = "Este método obtiene todas las cartas", description = "Muestra una lista de todas las cartas creadas")
    public ResponseEntity<CollectionModel<EntityModel<Carta>>> getAllCartas(){
        List <EntityModel<Carta>> listaCartas = cartaService.findAll().stream()
                .map(cartaAssembler::toModel)
                .collect(Collectors.toList());
        if(listaCartas.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(CollectionModel.of(
            listaCartas,
            linkTo(methodOn(CartaController.class).getAllCartas()).withSelfRel()
        ));
    }

    //get x id
    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Este método obtiene una carta en específico", description = "A través de un id, este método muestra una carta en específico")
    public ResponseEntity<EntityModel<Carta>> getCartaById(@Parameter(description = "Id del carta", required = true, example = "1")@PathVariable Long id){
        Carta carta = cartaService.findById(id);
        if (carta == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(cartaAssembler.toModel(carta));
    }

    //post crear un carta
    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Este método crea una carta", description = "Crea nueva carta, enviando un objeto a través de un POST")
    public ResponseEntity<EntityModel<Carta>> createCarta(@Parameter(description = "Detalles del carta", required = true)@RequestBody Carta carta){
        Carta newCarta = cartaService.save(carta);
        return ResponseEntity
                .created(linkTo(methodOn(CartaController.class).getCartaById(Long.valueOf(newCarta.getId()))).toUri())
                .body(cartaAssembler.toModel(newCarta));
    }

    //put para carta
    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Este método actualiza una carta", description = "A través de un id, este método actualiza una carta, pero se deben escribir todos los atributos")
    public ResponseEntity<EntityModel<Carta>> updateCarta(@Parameter(description = "Id del carta", required = true, example = "1")@PathVariable Long id, @Parameter(description = "Detalles del carta", required = true)@RequestBody Carta carta){
        carta.setId(carta.getId()); //revisar despues
        Carta updateCarta = cartaService.save(carta);
        return ResponseEntity.ok(cartaAssembler.toModel(updateCarta));
    }

    //Patch carta
    @PatchMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Este método puede modificar un campo en específico en una carta", description = "A través de un id, este método actualiza una carta, este método actualiza solo el atributo que nosotros queremos modificar")
    public ResponseEntity<EntityModel<Carta>> patchCarta(@Parameter(description = "Id del carta", required = true, example = "1")@PathVariable Long id, @Parameter(description = "Campo a modificar", required = true)@RequestBody Carta parcialCarta){
        Carta patched = cartaService.patchCarta(id, parcialCarta);
        if (patched == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(cartaAssembler.toModel(patched));
    }
    
    //delete carta
    @DeleteMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Este método puede eliminar una carta", description = "Elimina la carta especificada por el id")
    public ResponseEntity<Void> deleteCarta(@Parameter(description = "Id del carta", required = true, example = "1")@PathVariable Long id) {
        Carta cartaExistente = cartaService.findById(id);
        if (cartaExistente == null) {
            return ResponseEntity.notFound().build();
        }
        cartaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
