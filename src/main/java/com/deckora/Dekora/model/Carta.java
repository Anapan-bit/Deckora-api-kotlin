package com.deckora.Dekora.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
//import com.fasterxml.jackson.annotation.JsonManagedReference;


@Entity
@Table(name = "Carta")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Carta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre_carta;

    //Revisar si se tiene que sacar de esta tabla
    @Column(length = 2, nullable = false)
    private String estado;

    @Column(length = 100, nullable = false)
    private String descripcion;

    @Column(nullable = false)
    private String imagen_url;

    //Conexiones
    @ManyToMany
    @JoinTable( name = "carpeta_cartas",
        joinColumns = @JoinColumn(name = "carta_id"),
        inverseJoinColumns = @JoinColumn(name = "carpeta_id")
    )
    @JsonIgnoreProperties("cartas")
    private List<Carpeta> carpetas;

    @ManyToOne
    @JoinColumn(name = "categoria_id", nullable = true)
    @JsonIgnoreProperties("cartas")
    private Categoria categoria;
}
