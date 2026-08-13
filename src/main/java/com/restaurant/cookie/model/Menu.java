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
    private String description;
    
    @Column(nullable = false)
    private BigDecimal price;
    
    @Column(nullable = false)
    @Builder.Default
    private Integer status = 0; // 0 = available, 1 = not available
    
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "menu_ingredients",
        joinColumns = @JoinColumn(name = "menu_id"),
        inverseJoinColumns = @JoinColumn(name = "ingredient_id")
    )
    @Builder.Default
    private List<Ingredient> ingredients = new ArrayList<>();
}
