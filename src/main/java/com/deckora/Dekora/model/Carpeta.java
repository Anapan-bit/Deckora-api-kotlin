package com.deckora.Dekora.model;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Carpeta")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Carpeta {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 35)
    private String nombre_carpeta;

    @Column(nullable = false)
    private LocalDate fecha_creacion;

    //Conexiones
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    @JsonIgnoreProperties("carpetas")
    private Usuario usuario;

    @ManyToMany(mappedBy = "carpetas")
    @JsonIgnoreProperties("carpetas")
    private List<Carta> cartas;

    //Fecha el dia que es creada
        @PrePersist
    protected void onCreate() {
        this.fecha_creacion = LocalDate.now();
    }

}
