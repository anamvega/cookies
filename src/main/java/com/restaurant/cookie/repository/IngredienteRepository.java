package com.restaurant.cookie.repository;

import com.restaurant.cookie.model.Ingrediente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IngredienteRepository extends JpaRepository<Ingrediente, Long> {
    List<Ingrediente> findByRegistroId(Long registroId);
}
