package com.restaurant.cookie.controller;

import com.restaurant.cookie.model.Ingredient;
import com.restaurant.cookie.service.IngredienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ingredients")
@RequiredArgsConstructor
public class IngredientController {

    private final IngredienteService ingredienteService;

    /**
     * POST /api/ingredients
     * Crear un nuevo ingrediente
     */
    @PostMapping
    public ResponseEntity<Ingredient> crearIngrediente(@RequestBody Map<String, Object> request) {
        try {
            String name = (String) request.get("name");
            Object quantityRaw = request.get("quantity");
            Double quantity = quantityRaw instanceof Number ? ((Number) quantityRaw).doubleValue() : null;
            String unit = (String) request.get("unit");

            if (name == null || name.isBlank()) {
                throw new IllegalArgumentException("Ingredient name is required");
            }
            if (quantity == null) {
                throw new IllegalArgumentException("Quantity is required");
            }
            if (unit == null || unit.isBlank()) {
                throw new IllegalArgumentException("Unit of measure is required");
            }

            Ingredient ingredient = Ingredient.builder()
                    .name(name)
                    .quantity(quantity)
                    .unit(unit)
                    .build();

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ingredienteService.crearIngrediente(ingredient));
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error al crear ingrediente: " + e.getMessage(), e);
        }
    }

    /**
     * GET /api/ingredients
     * Obtener todos los ingredients
     */
    @GetMapping
    public ResponseEntity<List<Ingredient>> obtenerTodos() {
        return ResponseEntity.ok(ingredienteService.obtenerTodos());
    }

    /**
     * GET /api/ingredients/{id}
     * Obtener un ingrediente por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Ingredient> obtenerPorId(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(ingredienteService.obtenerPorId(id));
        } catch (RuntimeException e) {
            throw e;
        }
    }

    /**
     * PUT /api/ingredients/{id}
     * Actualizar un ingrediente
     */
    @PutMapping("/{id}")
    public ResponseEntity<Ingredient> actualizarIngrediente(
            @PathVariable Long id,
            @RequestBody Map<String, Object> request) {
        try {
            String name = (String) request.get("name");
            Object quantityRaw = request.get("quantity");
            Double quantity = quantityRaw instanceof Number ? ((Number) quantityRaw).doubleValue() : null;
            String unit = (String) request.get("unit");

            Ingredient ingredientUpdated = Ingredient.builder()
                    .name(name)
                    .quantity(quantity)
                    .unit(unit)
                    .build();

            return ResponseEntity.ok(ingredienteService.actualizarIngrediente(id, ingredientUpdated));
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar ingrediente: " + e.getMessage(), e);
        }
    }

    /**
     * DELETE /api/ingredients/{id}
     * Eliminar un ingrediente
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarIngrediente(@PathVariable Long id) {
        try {
            ingredienteService.eliminarIngrediente(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar ingrediente: " + e.getMessage(), e);
        }
    }
}
