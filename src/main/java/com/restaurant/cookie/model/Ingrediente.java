package com.restaurant.cookie.model;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "ingredientes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ingrediente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String nombre;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "registro_id", nullable = false)
    @JsonBackReference
    private Menu registro;
}
