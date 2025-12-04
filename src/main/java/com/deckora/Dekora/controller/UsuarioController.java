package com.deckora.Dekora.controller;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
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

import com.deckora.Dekora.assemblers.UsuarioModelAssembler;
import com.deckora.Dekora.model.LoginRequest;
import com.deckora.Dekora.model.Usuario;
import com.deckora.Dekora.service.UsuarioService;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@RequestMapping("/api/v2/usuarios")
//tag es para dar el titulo en el swagger de la usuarios
@Tag(name = "Usuarios", description = "Operaciones relacionadas con la creacion, modificación y eliminación de las usuarios.")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioModelAssembler usuarioAssembler;

    //get de todos las usuarios
    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    //operation es para el resumen de lo que hace
    //paramater es para describir el tipo de dato esperado
    @Operation(summary = "Este método obtiene todas las usuarios", description = "Muestra una lista de todas las usuarios creadas")
    public ResponseEntity<CollectionModel<EntityModel<Usuario>>> getAllUsuarios(){
        List <EntityModel<Usuario>> listaUsuarios = usuarioService.findAll().stream()
                .map(usuarioAssembler::toModel)
                .collect(Collectors.toList());
        if(listaUsuarios.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(CollectionModel.of(
            listaUsuarios,
            linkTo(methodOn(UsuarioController.class).getAllUsuarios()).withSelfRel()
        ));
    }

    //get x id
    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Este método obtiene una usuario en específico", description = "A través de un id, este método muestra una usuario en específico")
    public ResponseEntity<EntityModel<Usuario>> getUsuarioById(@Parameter(description = "Id del usuario", required = true, example = "1")@PathVariable Long id){
        Usuario usuario = usuarioService.findById(id);
        if (usuario == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(usuarioAssembler.toModel(usuario));
    }

    //post crear un usuario
    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Este método crea una usuario", description = "Crea nueva usuario, enviando un objeto a través de un POST")
    public ResponseEntity<EntityModel<Usuario>> createUsuario(@Parameter(description = "Detalles del usuario", required = true)@RequestBody Usuario usuario){
        Usuario newUsuario = usuarioService.save(usuario);
        return ResponseEntity
                .created(linkTo(methodOn(UsuarioController.class).getUsuarioById(Long.valueOf(newUsuario.getId()))).toUri())
                .body(usuarioAssembler.toModel(newUsuario));
    }

    //put para usuario
    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Este método actualiza una usuario", description = "A través de un id, este método actualiza una usuario, pero se deben escribir todos los atributos")
    public ResponseEntity<EntityModel<Usuario>> updateUsuario(@Parameter(description = "Id del usuario", required = true, example = "1")@PathVariable Long id, @Parameter(description = "Detalles del usuario", required = true)@RequestBody Usuario usuario){
        usuario.setId(usuario.getId()); //revisar despues
        Usuario updateUsuario = usuarioService.save(usuario);
        return ResponseEntity.ok(usuarioAssembler.toModel(updateUsuario));
    }

    //Patch usuario
    @PatchMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Este método puede modificar un campo en específico en una usuario", description = "A través de un id, este método actualiza una usuario, este método actualiza solo el atributo que nosotros queremos modificar")
    public ResponseEntity<EntityModel<Usuario>> patchUsuario(@Parameter(description = "Id del usuario", required = true, example = "1")@PathVariable Long id, @Parameter(description = "Campo a modificar", required = true)@RequestBody Usuario parcialUsuario){
        Usuario patched = usuarioService.patchUsuario(id, parcialUsuario);
        if (patched == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(usuarioAssembler.toModel(patched));
    }

    @PostMapping(value = "/login", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Login de usuario", description = "Verifica las credenciales y devuelve el usuario si son correctas")
    public ResponseEntity<?> loginUsuario(@Parameter(description = "Credenciales de login", required = true)@RequestBody LoginRequest loginRequest) {
        Optional<Usuario> usuarioOpcional = usuarioService.findByNombreUsuario(loginRequest.getNombre_usuario());

        if (usuarioOpcional.isPresent()) {
            Usuario usuario = usuarioOpcional.get();
            if (usuario.getContrasenia_usuario().equals(loginRequest.getContrasenia_usuario())) {
                // Devuelve el usuario como EntityModel
                return ResponseEntity.ok(usuarioAssembler.toModel(usuario));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                    .body("Contraseña incorrecta");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body("Usuario no encontrado");
        }
    }
}
