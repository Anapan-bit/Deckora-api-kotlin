package com.deckora.Dekora.repository;
import com.deckora.Dekora.model.Resumen;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;


@Repository
public interface ResumenRepository extends JpaRepository<Resumen, Long> {

}
