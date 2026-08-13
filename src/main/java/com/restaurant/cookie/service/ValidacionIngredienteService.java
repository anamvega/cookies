package com.restaurant.cookie.service;

import com.restaurant.cookie.model.Ingrediente;
import com.restaurant.cookie.repository.IngredienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Servicio para validar disponibilidad de ingredientes
 */
@Service
@RequiredArgsConstructor
public class ValidacionIngredienteService {

    private final IngredienteRepository ingredienteRepository;

    /**
     * Verifica si todos los ingredientes están disponibles en el inventario
     * 
     * @param ingredientesRequeridos Lista de ingredientes a validar
     * @return true si todos están disponibles, false en caso contrario
     */
    public boolean validarDisponibilidadIngredientes(List<Ingrediente> ingredientesRequeridos) {
        if (ingredientesRequeridos == null || ingredientesRequeridos.isEmpty()) {
            return true; // Si no hay ingredientes requeridos, está disponible
        }

        // Obtener todos los ingredientes disponibles
        List<Ingrediente> ingredientesDisponibles = ingredienteRepository.findAll();

        // Verificar cada ingrediente requerido
        for (Ingrediente requerido : ingredientesRequeridos) {
            boolean encontrado = ingredientesDisponibles.stream()
                    .anyMatch(disponible -> 
                        disponible.getNombre().equalsIgnoreCase(requerido.getNombre())
                        && disponible.getCantidad() != null 
                        && disponible.getCantidad() > 0
                    );

            if (!encontrado) {
                return false; // Si alguno no está disponible
            }
        }

        return true; // Todos están disponibles
    }

    /**
     * Obtiene detalles de qué ingredientes no están disponibles
     * 
     * @param ingredientesRequeridos Lista de ingredientes a validar
     * @return Lista de ingredientes no disponibles
     */
    public List<String> obtenerIngredientesNoDisponibles(List<Ingrediente> ingredientesRequeridos) {
        if (ingredientesRequeridos == null || ingredientesRequeridos.isEmpty()) {
            return List.of();
        }

        List<Ingrediente> ingredientesDisponibles = ingredienteRepository.findAll();

        return ingredientesRequeridos.stream()
                .filter(requerido -> !ingredientesDisponibles.stream()
                        .anyMatch(disponible -> 
                            disponible.getNombre().equalsIgnoreCase(requerido.getNombre())
                            && disponible.getCantidad() != null 
                            && disponible.getCantidad() > 0
                        )
                )
                .map(Ingrediente::getNombre)
                .toList();
    }
}
