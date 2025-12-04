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

import com.deckora.Dekora.assemblers.CarpetaModelAssembler;
import com.deckora.Dekora.model.Carpeta;
import com.deckora.Dekora.model.Resumen;
import com.deckora.Dekora.service.CarpetaService;
import com.deckora.Dekora.service.ResumenService;
import com.deckora.Dekora.service.UsuarioService;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v2/carpetas")
// tag es para dar el titulo en el swagger de la carpetas
@Tag(name = "Carpetas", description = "Operaciones relacionadas con la creacion, modificación y eliminación de las carpetas.")
public class CarpetaController {

    @Autowired
    private CarpetaService carpetaService;

    @Autowired
    private CarpetaModelAssembler carpetaAssembler;

    @Autowired
    private ResumenService resumenService;

    @Autowired
    private UsuarioService usuarioService;

    // get de todos las carpetas
    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    // operation es para el resumen de lo que hace
    // paramater es para describir el tipo de dato esperado
    @Operation(summary = "Este método obtiene todas las carpetas", description = "Muestra una lista de todas las carpetas creadas")
    public ResponseEntity<CollectionModel<EntityModel<Carpeta>>> getAllCarpetas() {
        List<EntityModel<Carpeta>> listaCarpetas = carpetaService.findAll().stream()
                .map(carpetaAssembler::toModel)
                .collect(Collectors.toList());
        if (listaCarpetas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(CollectionModel.of(
                listaCarpetas,
                linkTo(methodOn(CarpetaController.class).getAllCarpetas()).withSelfRel()));
    }

    // get x id
    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Este método obtiene una carpeta en específico", description = "A través de un id, este método muestra una carpeta en específico")
    public ResponseEntity<EntityModel<Carpeta>> getCarpetaById(
            @Parameter(description = "Id del carpeta", required = true, example = "1") @PathVariable Long id) {
        Carpeta carpeta = carpetaService.findById(id);
        if (carpeta == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(carpetaAssembler.toModel(carpeta));
    }

    // post crear un carpeta
    @PostMapping("/usuario/{idUsuario}")
    @Operation(summary = "Crea una carpeta y la asocia a un usuario")
    public ResponseEntity<EntityModel<Carpeta>> createCarpetaAsignada(
            @PathVariable Long idUsuario,
            @RequestBody Carpeta carpeta) {

        // 1. Crear carpeta
        Carpeta newCarpeta = carpetaService.save(carpeta);

        // 2. Crear resumen que relacione usuario + carpeta
        Resumen resumen = new Resumen();
        resumen.setUsuario(usuarioService.findById(idUsuario));
        resumen.setCarpeta(newCarpeta);
        resumen.setCarta(null);

        resumenService.save(resumen);

        return ResponseEntity
                .created(linkTo(methodOn(CarpetaController.class)
                        .getCarpetaById(newCarpeta.getId()))
                        .toUri())
                .body(carpetaAssembler.toModel(newCarpeta));
    }

    // put para carpeta
    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Este método actualiza una carpeta", description = "A través de un id, este método actualiza una carpeta, pero se deben escribir todos los atributos")
    public ResponseEntity<EntityModel<Carpeta>> updateCarpeta(
            @Parameter(description = "Id del carpeta", required = true, example = "1") @PathVariable Long id,
            @Parameter(description = "Detalles del carpeta", required = true) @RequestBody Carpeta carpeta) {
        carpeta.setId(carpeta.getId()); // revisar despues
        Carpeta updateCarpeta = carpetaService.save(carpeta);
        return ResponseEntity.ok(carpetaAssembler.toModel(updateCarpeta));
    }

    // Patch carpeta
    @PatchMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Este método puede modificar un campo en específico en una carpeta", description = "A través de un id, este método actualiza una carpeta, este método actualiza solo el atributo que nosotros queremos modificar")
    public ResponseEntity<EntityModel<Carpeta>> patchCarpeta(
            @Parameter(description = "Id del carpeta", required = true, example = "1") @PathVariable Long id,
            @Parameter(description = "Campo a modificar", required = true) @RequestBody Carpeta parcialCarpeta) {
        Carpeta patched = carpetaService.patchCarpeta(id, parcialCarpeta);
        if (patched == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(carpetaAssembler.toModel(patched));
    }

    // delete carpeta
    /*
     * @DeleteMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
     * 
     * @Operation(summary = "Este método puede eliminar una carpeta", description =
     * "Elimina la carpeta especificada por el id")
     * public ResponseEntity<Void> deleteCarpeta(@Parameter(description =
     * "Id del carpeta", required = true, example = "1")@PathVariable Long id) {
     * Carpeta carpetaExistente = carpetaService.findById(id);
     * if (carpetaExistente == null) {
     * return ResponseEntity.notFound().build();
     * }
     * carpetaService.delete(id);
     * return ResponseEntity.noContent().build();
     * }
     */
}
