package com.restaurant.cookie.service;

import com.restaurant.cookie.model.Ingredient;
import com.restaurant.cookie.repository.IngredientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Servicio para validar disponibilidad de ingredients
 */
@Service
@RequiredArgsConstructor
public class ValidacionIngredienteService {

    private final IngredientRepository ingredientRepository;

    /**
     * Verifica si todos los ingredients están disponibles en el inventario
     * 
     * @param ingredientesRequeridos Lista de ingredients a validar
     * @return true si todos están disponibles, false en caso contrario
     */
    public boolean validarDisponibilidadIngredientes(List<Ingredient> ingredientesRequeridos) {
        if (ingredientesRequeridos == null || ingredientesRequeridos.isEmpty()) {
            return true; // Si no hay ingredients requeridos, está disponible
        }

        // Obtener todos los ingredients disponibles
        List<Ingredient> ingredientesDisponibles = ingredientRepository.findAll();

        // Verify each required ingredient
        for (Ingredient required : ingredientesRequeridos) {
            boolean found = ingredientesDisponibles.stream()
                    .anyMatch(available -> 
                        available.getName().equalsIgnoreCase(required.getName())
                        && available.getQuantity() != null 
                        && available.getQuantity() > 0
                    );

            if (!found) {
                return false;
            }
        }

        return true; // Todos están disponibles
    }

    /**
     * Obtiene detalles de qué ingredients no están disponibles
     * 
     * @param ingredientesRequeridos Lista de ingredients a validar
     * @return Lista de ingredients no disponibles
     */
    public List<String> obtenerIngredientesNoDisponibles(List<Ingredient> ingredientesRequeridos) {
        if (ingredientesRequeridos == null || ingredientesRequeridos.isEmpty()) {
            return List.of();
        }

        List<Ingredient> ingredientesDisponibles = ingredientRepository.findAll();

        return ingredientesRequeridos.stream()
                .filter(required -> !ingredientesDisponibles.stream()
                        .anyMatch(available -> 
                            available.getName().equalsIgnoreCase(required.getName())
                            && available.getQuantity() != null 
                            && available.getQuantity() > 0
                        )
                )
                .map(Ingredient::getName)
                .toList();
    }
}
