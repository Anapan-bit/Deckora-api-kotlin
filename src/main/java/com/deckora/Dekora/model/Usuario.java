package com.deckora.Dekora.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Usuario")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Usuario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String contrasenia_usuario;

    @Column(length = 50, nullable = false)
    private String nombre_usuario;

    @Column(length = 50, nullable = false)
    private String apellido_usuario;

    @Column(length = 50, nullable = false)
    private String correo_usuario;

    @Column(length = 9, nullable = false)
    private Integer numero_telefono;

    //Conexiones



}