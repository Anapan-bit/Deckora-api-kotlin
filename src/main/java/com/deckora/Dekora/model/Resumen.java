package com.deckora.Dekora.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "Resumen")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Resumen {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = true) //Revisar si puede ser null
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "id_carta", nullable = true)
    private Carta carta;

    @ManyToOne
    @JoinColumn(name = "id_carpeta", nullable = true)
    private Carpeta carpeta;
}