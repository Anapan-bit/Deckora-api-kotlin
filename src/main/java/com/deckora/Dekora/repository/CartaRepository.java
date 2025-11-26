package com.deckora.Dekora.repository;
import com.deckora.Dekora.model.Carta;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;


@Repository
public interface CartaRepository extends JpaRepository<Carta, Long> {

}
