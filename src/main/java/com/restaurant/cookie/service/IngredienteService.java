package com.restaurant.cookie.service;

import com.restaurant.cookie.model.Ingredient;
import com.restaurant.cookie.repository.IngredientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IngredienteService {

    private final IngredientRepository ingredientRepository;

    /**
     * Crear un nuevo ingredient
     */
    public Ingredient crearIngrediente(Ingredient ingredient) {
        validarIngrediente(ingredient);
        return ingredientRepository.save(ingredient);
    }

    /**
     * Obtener todos los ingredients
     */
    public List<Ingredient> obtenerTodos() {
        return ingredientRepository.findAll();
    }

    /**
     * Obtener un ingrediente por ID
     */
    public Ingredient obtenerPorId(Long id) {
        return ingredientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ingredient no encontrado con id: " + id));
    }

    /**
     * Actualizar un ingrediente
     */
    public Ingredient actualizarIngrediente(Long id, Ingredient ingredientUpdated) {
        Ingredient ingredient = obtenerPorId(id);
        
        if (ingredientUpdated.getName() != null && !ingredientUpdated.getName().isBlank()) {
            ingredient.setName(ingredientUpdated.getName());
        }
        
        if (ingredientUpdated.getQuantity() != null) {
            ingredient.setQuantity(ingredientUpdated.getQuantity());
        }
        
        if (ingredientUpdated.getUnit() != null && !ingredientUpdated.getUnit().isBlank()) {
            ingredient.setUnit(ingredientUpdated.getUnit());
        }
        
        validarIngrediente(ingredient);
        return ingredientRepository.save(ingredient);
    }

    /**
     * Eliminar un ingrediente
     */
    public void eliminarIngrediente(Long id) {
        if (!ingredientRepository.existsById(id)) {
            throw new RuntimeException("Ingredient no encontrado con id: " + id);
        }
        ingredientRepository.deleteById(id);
    }

    /**
     * Validar los datos del ingredient
     */
    private void validarIngrediente(Ingredient ingredient) {
        if (ingredient.getName() == null || ingredient.getName().isBlank()) {
            throw new IllegalArgumentException("Ingredient name is required");
        }
        
        if (ingredient.getQuantity() == null || ingredient.getQuantity() <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }
        
        if (ingredient.getUnit() == null || ingredient.getUnit().isBlank()) {
            throw new IllegalArgumentException("Unit of measure is required");
        }
    }
}
