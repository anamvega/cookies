package com.restaurant.cookie.repository;

import com.restaurant.cookie.model.Registro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface RegistroRepository extends JpaRepository<Registro, Long> {
    List<Registro> findAll();

    // Active Record style queries
    List<Registro> findByDescripcion(String descripcion);

    List<Registro> findByPrecioGreaterThan(BigDecimal precio);

    List<Registro> findByPrecioLessThan(BigDecimal precio);

    Optional<Registro> findById(Long id);

    boolean existsByDescripcion(String descripcion);

    Long count();

    void deleteById(Long id);
}
