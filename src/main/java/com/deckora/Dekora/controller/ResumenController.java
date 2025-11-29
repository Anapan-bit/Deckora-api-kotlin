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

import com.deckora.Dekora.assemblers.ResumenModelAssembler;
import com.deckora.Dekora.model.Resumen;
import com.deckora.Dekora.service.ResumenService;


import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@RequestMapping("/api/v2/resumenes")
//tag es para dar el titulo en el swagger de los resumenes
@Tag(name = "resumenes", description = "Operaciones relacionadas con la creacion, modificación y eliminación de los resumenes.")
public class ResumenController {

    @Autowired
    private ResumenService resumenService;

    @Autowired
    private ResumenModelAssembler resumenAssembler;

    //get de todos las resumenes
    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Este método obtiene todas las resumenes", description = "Muestra una lista de todas los resumenes creadas")
    public ResponseEntity<CollectionModel<EntityModel<Resumen>>> getAllResumenes(){
        List <EntityModel<Resumen>> listaResumen = resumenService.findAll().stream()
                .map(resumenAssembler::toModel)
                .collect(Collectors.toList());
        if(listaResumen.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(CollectionModel.of(
            listaResumen,
            linkTo(methodOn(ResumenController.class).getAllResumenes()).withSelfRel()
        ));
    }

    //get x id
    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Este método obtiene un resumen en específico", description = "A través de un id, este método muestra un resumen en específico")
    public ResponseEntity<EntityModel<Resumen>> getResumenById(@Parameter(description = "Id del resumen", required = true, example = "1")@PathVariable Long id){
        Resumen resumen = resumenService.findById(id);
        if (resumen == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(resumenAssembler.toModel(resumen));
    }

    //post crear un resumen
    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Este método crea resumen", description = "Crea nuevo resumen, enviando un objeto a través de un POST")
    public ResponseEntity<EntityModel<Resumen>> createResumen(@Parameter(description = "Detalles del resumen", required = true)@RequestBody Resumen resumen){
        Resumen newResumen = resumenService.save(resumen);
        return ResponseEntity
                .created(linkTo(methodOn(ResumenController.class).getResumenById(Long.valueOf(newResumen.getId()))).toUri())
                .body(resumenAssembler.toModel(newResumen));
    }

    //put para usuario
    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Este método actualiza un resumen", description = "A través de un id, este método actualiza una resumen, pero se deben escribir todos los atributos")
    public ResponseEntity<EntityModel<Resumen>> updateResumen(@Parameter(description = "Id del resumen", required = true, example = "1")@PathVariable Long id, @Parameter(description = "Detalles del resumen", required = true)@RequestBody Resumen resumen){
        resumen.setId(resumen.getId()); //revisar despues
        Resumen updateResumen = resumenService.save(resumen);
        return ResponseEntity.ok(resumenAssembler.toModel(updateResumen));
    }

/*     //Patch usuario
    @PatchMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Este método puede modificar un campo en específico en una usuario", description = "A través de un id, este método actualiza una usuario, este método actualiza solo el atributo que nosotros queremos modificar")
    public ResponseEntity<EntityModel<Usuario>> patchUsuario(@Parameter(description = "Id del usuario", required = true, example = "1")@PathVariable Long id, @Parameter(description = "Campo a modificar", required = true)@RequestBody Usuario parcialUsuario){
        Usuario patched = usuarioService.patchUsuario(id, parcialUsuario);
        if (patched == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(usuarioAssembler.toModel(patched));
    } */
    
    //delete usuario
/*     @DeleteMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Este método puede eliminar una usuario", description = "Elimina la usuario especificada por el id")
    public ResponseEntity<Void> deleteUsuario(@Parameter(description = "Id del usuario", required = true, example = "1")@PathVariable Long id) {
        Usuario usuarioExistente = usuarioService.findById(id);
        if (usuarioExistente == null) {
            return ResponseEntity.notFound().build();
        }
        usuarioService.delete(id);
        return ResponseEntity.noContent().build();
    } */
}
