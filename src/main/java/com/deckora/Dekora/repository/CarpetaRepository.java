package com.deckora.Dekora.repository;
import com.deckora.Dekora.model.Carpeta;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;


@Repository
public interface CarpetaRepository extends JpaRepository<Carpeta, Long> {

}
