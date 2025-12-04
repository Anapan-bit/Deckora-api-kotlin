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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.deckora.Dekora.assemblers.ResumenModelAssembler;
import com.deckora.Dekora.model.Carpeta;
import com.deckora.Dekora.model.Carta;
import com.deckora.Dekora.model.Resumen;
import com.deckora.Dekora.model.Usuario;
import com.deckora.Dekora.service.ResumenService;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v2/resumenes")
// tag es para dar el titulo en el swagger de los resumenes
@Tag(name = "Resumenes", description = "Operaciones relacionadas con la creacion, modificación y eliminación de los resumenes.")
public class ResumenController {

    @Autowired
    private ResumenService resumenService;

    @Autowired
    private ResumenModelAssembler resumenAssembler;

    // get de todos las resumenes
    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Este método obtiene todas las resumenes", description = "Muestra una lista de todas los resumenes creadas")
    public ResponseEntity<CollectionModel<EntityModel<Resumen>>> getAllResumenes() {
        List<EntityModel<Resumen>> listaResumen = resumenService.findAll().stream()
                .map(resumenAssembler::toModel)
                .collect(Collectors.toList());
        if (listaResumen.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(CollectionModel.of(
                listaResumen,
                linkTo(methodOn(ResumenController.class).getAllResumenes()).withSelfRel()));
    }

    // get x id
    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Este método obtiene un resumen en específico", description = "A través de un id, este método muestra un resumen en específico")
    public ResponseEntity<EntityModel<Resumen>> getResumenById(
            @Parameter(description = "Id del resumen", required = true, example = "1") @PathVariable Long id) {
        Resumen resumen = resumenService.findById(id);
        if (resumen == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(resumenAssembler.toModel(resumen));
    }

    // post crear un resumen
    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Este método crea resumen", description = "Crea nuevo resumen, enviando un objeto a través de un POST")
    public ResponseEntity<EntityModel<Resumen>> createResumen(
            @Parameter(description = "Detalles del resumen", required = true) @RequestBody Resumen resumen) {
        Resumen newResumen = resumenService.save(resumen);
        return ResponseEntity
                .created(linkTo(methodOn(ResumenController.class).getResumenById(Long.valueOf(newResumen.getId())))
                        .toUri())
                .body(resumenAssembler.toModel(newResumen));
    }

    // put para resumen
    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Este método actualiza un resumen", description = "A través de un id, este método actualiza una resumen, pero se deben escribir todos los atributos")
    public ResponseEntity<EntityModel<Resumen>> updateResumen(
            @Parameter(description = "Id del resumen", required = true, example = "1") @PathVariable Long id,
            @Parameter(description = "Detalles del resumen", required = true) @RequestBody Resumen resumen) {
        resumen.setId(resumen.getId()); // revisar despues
        Resumen updateResumen = resumenService.save(resumen);
        return ResponseEntity.ok(resumenAssembler.toModel(updateResumen));
    }

    // delete
    // resumen(usuario)
    @DeleteMapping(value = "/{id}/usuario", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Este método puede eliminar un usuario", description = "Elimina un usuario especificado por el id del resumen")
    public ResponseEntity<Void> deleteUsuario(
            @Parameter(description = "Id del resumen", required = true, example = "1") @PathVariable Long id) {
        Resumen resumenExistente = resumenService.findById(id);
        if (resumenExistente == null) {
            return ResponseEntity.notFound().build();
        }
        resumenService.borrarUsuario(id);
        ;
        return ResponseEntity.noContent().build();
    }

    // carta
    @DeleteMapping(value = "/{id}/carta", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Este método puede eliminar una carta desde un resumen", description = "Elimina una carta desde un resumen especificada por el id")
    public ResponseEntity<Void> deleteCarta(
            @Parameter(description = "Id del resumen", required = true, example = "1") @PathVariable Long id) {
        Resumen resumenExistente = resumenService.findById(id);
        if (resumenExistente == null) {
            return ResponseEntity.notFound().build();
        }
        resumenService.borrarCarta(id);
        ;
        return ResponseEntity.noContent().build();
    }

    // Carpeta
    @DeleteMapping(value = "/{id}/carpeta", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Este método puede eliminar una carpeta desde un resumen", description = "Elimina una carpeta desde un resumen especificada por el id")
    public ResponseEntity<Void> deleteCarpeta(
            @Parameter(description = "Id del resumen", required = true, example = "1") @PathVariable Long id) {
        Resumen resumenExistente = resumenService.findById(id);
        if (resumenExistente == null) {
            return ResponseEntity.notFound().build();
        }
        resumenService.borrarCarpeta(id);
        ;
        return ResponseEntity.noContent().build();
    }

    // Patch para mover carta de carpeta
    @PatchMapping(value = "/{id}/mover-carta", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Mueve una carta a otra carpeta", description = "Actualiza la carpeta asociada a un resumen")
    public ResponseEntity<EntityModel<Resumen>> moverCarta(
            @Parameter(description = "Id del resumen", required = true) @PathVariable Long id,
            @Parameter(description = "Id de la nueva carpeta", required = true) @RequestParam Long idCarpetaNueva) {

        Resumen resumenActualizado = resumenService.moverCarta(id, idCarpetaNueva);

        return ResponseEntity.ok(resumenAssembler.toModel(resumenActualizado));
    }

    // Querys
    // Carpetas de un usuario
    @GetMapping("/usuario/{idUsuario}/carpetas")
    @Operation(summary = "Obtiene las carpetas asociadas a un usuario", description = "Devuelve todas las carpetas por el id del usuario")
    public ResponseEntity<List<Carpeta>> getCarpetasByUsuario(@PathVariable Long idUsuario) {
        return ResponseEntity.ok(resumenService.getCarpetasByUsuario(idUsuario));
    }

    // cartas de una carpeta
    @GetMapping("/carpeta/{idCarpeta}/cartas")
    @Operation(summary = "Obtiene las cartas asociadas a una carpeta", description = "Devuelve todas las cartas en una carpeta por el id de la carpeta")
    public ResponseEntity<List<Carta>> getCartasByCarpeta(@PathVariable Long idCarpeta) {
        return ResponseEntity.ok(resumenService.getCartasByCarpeta(idCarpeta));
    }

    // detalles usuario
    @GetMapping("/usuario/{idUsuario}")
    @Operation(summary = "Obtiene los detalles de un usuario", description = "Devuelve los datos del usuario según su ID")
    public ResponseEntity<Usuario> getUsuarioById(@PathVariable Long idUsuario) {
        Usuario usuario = resumenService.getUsuarioById(idUsuario);
        return ResponseEntity.ok(usuario);
    }

}
