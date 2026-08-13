package com.restaurant.cookie.config;

import com.restaurant.cookie.model.Ingrediente;
import com.restaurant.cookie.repository.IngredienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Cargador de datos iniciales
 * Crea ingredientes por defecto al iniciar la aplicación
 */
@Component
@RequiredArgsConstructor
public class InitDataLoader implements CommandLineRunner {

    private final IngredienteRepository ingredienteRepository;

    @Override
    public void run(String... args) throws Exception {
        // Verificar si ya existen ingredientes
        if (ingredienteRepository.count() > 0) {
            return; // No cargar si ya hay datos
        }

        // Crear ingredientes por defecto
        List<Ingrediente> ingredientesDefault = List.of(
            Ingrediente.builder()
                .nombre("Harina")
                .cantidad(1000.0)
                .unidad("g")
                .build(),
            
            Ingrediente.builder()
                .nombre("Azúcar")
                .cantidad(500.0)
                .unidad("g")
                .build(),
            
            Ingrediente.builder()
                .nombre("Mantequilla")
                .cantidad(250.0)
                .unidad("g")
                .build(),
            
            Ingrediente.builder()
                .nombre("Huevos")
                .cantidad(12.0)
                .unidad("unidad")
                .build(),
            
            Ingrediente.builder()
                .nombre("Chocolate")
                .cantidad(500.0)
                .unidad("g")
                .build(),
            
            Ingrediente.builder()
                .nombre("Leche")
                .cantidad(1.0)
                .unidad("l")
                .build(),
            
            Ingrediente.builder()
                .nombre("Vainilla")
                .cantidad(100.0)
                .unidad("ml")
                .build(),
            
            Ingrediente.builder()
                .nombre("Polvo de Hornear")
                .cantidad(100.0)
                .unidad("g")
                .build(),
            
            Ingrediente.builder()
                .nombre("Sal")
                .cantidad(50.0)
                .unidad("g")
                .build(),
            
            Ingrediente.builder()
                .nombre("Cacao en Polvo")
                .cantidad(200.0)
                .unidad("g")
                .build()
        );

        ingredienteRepository.saveAll(ingredientesDefault);
        System.out.println("✓ Se cargaron " + ingredientesDefault.size() + " ingredientes por defecto");
    }
}
