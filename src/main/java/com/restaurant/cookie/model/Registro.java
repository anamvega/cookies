package com.restaurant.cookie.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Component
@Entity
@Table(name = "registros")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Registro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String descripcion;
    
    private BigDecimal precio;

    @Autowired
    private static EntityManager entityManager;

    // ==================== Active Record: Métodos estáticos ====================

    /** Obtiene todos los registros */
    public static List<Registro> findAll() {
        return entityManager.createQuery("SELECT r FROM Registro r", Registro.class)
                .getResultList();
    }

    /** Busca un registro por ID */
    public static Optional<Registro> findById(Long id) {
        Registro registro = entityManager.find(Registro.class, id);
        return Optional.ofNullable(registro);
    }

    /** Busca registros por descripción */
    public static List<Registro> findByDescripcion(String descripcion) {
        return entityManager.createQuery(
                "SELECT r FROM Registro r WHERE r.descripcion = :descripcion", Registro.class)
                .setParameter("descripcion", descripcion)
                .getResultList();
    }

    /** Busca registros con precio mayor al especificado */
    public static List<Registro> findByPrecioGreaterThan(BigDecimal precio) {
        return entityManager.createQuery(
                "SELECT r FROM Registro r WHERE r.precio > :precio", Registro.class)
                .setParameter("precio", precio)
                .getResultList();
    }

    /** Busca registros con precio menor al especificado */
    public static List<Registro> findByPrecioLessThan(BigDecimal precio) {
        return entityManager.createQuery(
                "SELECT r FROM Registro r WHERE r.precio < :precio", Registro.class)
                .setParameter("precio", precio)
                .getResultList();
    }

    /** Cuenta el total de registros */
    public static long count() {
        Long total = entityManager.createQuery(
                "SELECT COUNT(r) FROM Registro r", Long.class)
                .getSingleResult();
        return total;
    }

    /** Guarda o actualiza un registro */
    public static Registro save(Registro registro) {
        if (registro.getId() == null) {
            entityManager.persist(registro);
            return registro;
        }
        return entityManager.merge(registro);
    }

    /** Elimina un registro */
    public static void delete(Registro registro) {
        Registro managed = entityManager.find(Registro.class, registro.getId());
        if (managed != null) {
            entityManager.remove(managed);
        }
    }

    // ==================== Active Record: Métodos de instancia ====================

    /** Guarda el registro actual (instancia) */
    public Registro save() {
        return Registro.save(this);
    }

    /** Elimina el registro actual (instancia) */
    public void delete() {
        Registro.delete(this);
    }
}
