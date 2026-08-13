package com.restaurant.cookie.controller;

import com.restaurant.cookie.model.Ingrediente;
import com.restaurant.cookie.service.IngredienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ingredientes")
@RequiredArgsConstructor
public class IngredienteController {

    private final IngredienteService ingredienteService;

    /**
     * POST /api/ingredientes
     * Crear un nuevo ingrediente
     */
    @PostMapping
    public ResponseEntity<Ingrediente> crearIngrediente(@RequestBody Map<String, Object> request) {
        try {
            String nombre = (String) request.get("nombre");
            Object cantidadRaw = request.get("cantidad");
            Double cantidad = cantidadRaw instanceof Number ? ((Number) cantidadRaw).doubleValue() : null;
            String unidad = (String) request.get("unidad");

            if (nombre == null || nombre.isBlank()) {
                throw new IllegalArgumentException("El nombre del ingrediente es obligatorio");
            }
            if (cantidad == null) {
                throw new IllegalArgumentException("La cantidad es obligatoria");
            }
            if (unidad == null || unidad.isBlank()) {
                throw new IllegalArgumentException("La unidad de medida es obligatoria");
            }

            Ingrediente ingrediente = Ingrediente.builder()
                    .nombre(nombre)
                    .cantidad(cantidad)
                    .unidad(unidad)
                    .build();

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ingredienteService.crearIngrediente(ingrediente));
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error al crear ingrediente: " + e.getMessage(), e);
        }
    }

    /**
     * GET /api/ingredientes
     * Obtener todos los ingredientes
     */
    @GetMapping
    public ResponseEntity<List<Ingrediente>> obtenerTodos() {
        return ResponseEntity.ok(ingredienteService.obtenerTodos());
    }

    /**
     * GET /api/ingredientes/{id}
     * Obtener un ingrediente por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Ingrediente> obtenerPorId(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(ingredienteService.obtenerPorId(id));
        } catch (RuntimeException e) {
            throw e;
        }
    }

    /**
     * PUT /api/ingredientes/{id}
     * Actualizar un ingrediente
     */
    @PutMapping("/{id}")
    public ResponseEntity<Ingrediente> actualizarIngrediente(
            @PathVariable Long id,
            @RequestBody Map<String, Object> request) {
        try {
            String nombre = (String) request.get("nombre");
            Object cantidadRaw = request.get("cantidad");
            Double cantidad = cantidadRaw instanceof Number ? ((Number) cantidadRaw).doubleValue() : null;
            String unidad = (String) request.get("unidad");

            Ingrediente ingredienteActualizado = Ingrediente.builder()
                    .nombre(nombre)
                    .cantidad(cantidad)
                    .unidad(unidad)
                    .build();

            return ResponseEntity.ok(ingredienteService.actualizarIngrediente(id, ingredienteActualizado));
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar ingrediente: " + e.getMessage(), e);
        }
    }

    /**
     * DELETE /api/ingredientes/{id}
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
