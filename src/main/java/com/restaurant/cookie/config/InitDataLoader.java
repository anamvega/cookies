package com.restaurant.cookie.config;

import com.restaurant.cookie.model.Ingredient;
import com.restaurant.cookie.repository.IngredientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Cargador de datos iniciales
 * Crea ingredients por defecto al iniciar la aplicación
 */
@Component
@RequiredArgsConstructor
public class InitDataLoader implements CommandLineRunner {

    private final IngredientRepository ingredientRepository;

    @Override
    public void run(String... args) throws Exception {
        // Verificar si ya existen ingredients
        if (ingredientRepository.count() > 0) {
            return; // No cargar si ya hay datos
        }

        // Create default ingredients
        List<Ingredient> defaultIngredients = List.of(
            Ingredient.builder()
                .name("Flour")
                .quantity(1000.0)
                .unit("g")
                .build(),
            
            Ingredient.builder()
                .name("Sugar")
                .quantity(500.0)
                .unit("g")
                .build(),
            
            Ingredient.builder()
                .name("Butter")
                .quantity(250.0)
                .unit("g")
                .build(),
            
            Ingredient.builder()
                .name("Eggs")
                .quantity(12.0)
                .unit("unit")
                .build(),
            
            Ingredient.builder()
                .name("Chocolate")
                .quantity(500.0)
                .unit("g")
                .build(),
            
            Ingredient.builder()
                .name("Milk")
                .quantity(1.0)
                .unit("l")
                .build(),
            
            Ingredient.builder()
                .name("Vanilla")
                .quantity(100.0)
                .unit("ml")
                .build(),
            
            Ingredient.builder()
                .name("Baking Powder")
                .quantity(100.0)
                .unit("g")
                .build(),
            
            Ingredient.builder()
                .name("Salt")
                .quantity(50.0)
                .unit("g")
                .build(),
            
            Ingredient.builder()
                .name("Cocoa Powder")
                .quantity(200.0)
                .unit("g")
                .build()
        );

        ingredientRepository.saveAll(defaultIngredients);
        System.out.println("✓ Se cargaron " + defaultIngredients.size() + " ingredients por defecto");
    }
}
