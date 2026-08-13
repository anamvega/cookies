package com.restaurant.cookie.repository;

import com.restaurant.cookie.model.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {
    List<Menu> findAll();

    // Active Record style queries
    List<Menu> findByDescripcion(String descripcion);

    List<Menu> findByPrecioGreaterThan(BigDecimal precio);

    List<Menu> findByPrecioLessThan(BigDecimal precio);

    Optional<Menu> findById(Long id);

    boolean existsByDescripcion(String descripcion);

    void deleteById(Long id);
}
