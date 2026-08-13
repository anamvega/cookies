package com.restaurant.cookie.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "menu")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String descripcion;
    
    @Column(nullable = false)
    private BigDecimal precio;
    
    @Column(nullable = false)
    @Builder.Default
    private Integer estado = 0; // 0 = disponible, 1 = no disponible
    
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "menu_ingredientes",
        joinColumns = @JoinColumn(name = "menu_id"),
        inverseJoinColumns = @JoinColumn(name = "ingrediente_id")
    )
    @Builder.Default
    private List<Ingrediente> ingredientes = new ArrayList<>();
}
