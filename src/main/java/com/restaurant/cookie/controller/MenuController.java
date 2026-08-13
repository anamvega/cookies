package com.restaurant.cookie.controller;

import com.restaurant.cookie.model.Menu;
import com.restaurant.cookie.service.MenuService;
import com.restaurant.cookie.service.ValidacionIngredienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/registros")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;
    private final ValidacionIngredienteService validacionIngredienteService;

    @PostMapping
    public ResponseEntity<Menu> crearNuevoPlato(@RequestBody Map<String, Object> request) {
        try {
            String description = (String) request.get("description");
            Object priceRaw = request.get("price");
            Double price = priceRaw instanceof Number ? ((Number) priceRaw).doubleValue() : null;
            
            @SuppressWarnings("unchecked")
            List<Integer> ingredientesIds = (List<Integer>) request.get("ingredientesIds");

            if (description == null || description.isBlank()) {
                throw new IllegalArgumentException("Description is required");
            }
            if (price == null) {
                throw new IllegalArgumentException("Price is required");
            }

            Menu registro = Menu.builder()
                    .description(description)
                    .price(BigDecimal.valueOf(price))
                    .build();

            Menu registroGuardado = menuService.crearRegistro(registro);
            
            // Agregar ingredients por ID después de guardar el registro
            if (ingredientesIds != null && !ingredientesIds.isEmpty()) {
                registroGuardado = menuService.agregarIngredientesAlRegistro(registroGuardado.getId(), ingredientesIds);
            }
            
            return ResponseEntity.status(HttpStatus.CREATED).body(registroGuardado);
        } catch (IllegalArgumentException e) {
            throw e; // Relanzar para que lo capture el GlobalExceptionHandler
        } catch (Exception e) {
            throw new RuntimeException("Error al crear el plato: " + e.getMessage(), e);
        }
    }

    @GetMapping
    public ResponseEntity<List<Menu>> obtenerTodos() {
        try {
            List<Menu> registros = menuService.obtenerTodosLosRegistros();
            return ResponseEntity.ok(registros);
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener los registros: " + e.getMessage(), e);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Menu> obtenerPorId(@PathVariable Long id) {
        try {
            Menu registro = menuService.obtenerRegistroPorId(id);
            return ResponseEntity.ok(registro);
        } catch (RuntimeException e) {
            throw e;
        }
    }

    /**
     * GET /registros/disponibles
     * Obtener solo los registros disponibles (estado = 0)
     */
    @GetMapping("/estado/disponibles")
    public ResponseEntity<List<Menu>> obtenerDisponibles() {
        try {
            List<Menu> registros = menuService.obtenerRegistrosDisponibles();
            return ResponseEntity.ok(registros);
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener registros disponibles: " + e.getMessage(), e);
        }
    }

    /**
     * GET /registros/no-disponibles
     * Obtener solo los registros no disponibles (estado = 1)
     */
    @GetMapping("/estado/no-disponibles")
    public ResponseEntity<List<Menu>> obtenerNoDisponibles() {
        try {
            List<Menu> registros = menuService.obtenerRegistrosNoDisponibles();
            return ResponseEntity.ok(registros);
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener registros no disponibles: " + e.getMessage(), e);
        }
    }

    /**
     * GET /registros/{id}/ingredients-faltantes
     * Obtener ingredients que faltan para un registro específico
     */
    @GetMapping("/{id}/ingredients-faltantes")
    public ResponseEntity<Map<String, Object>> obtenerIngredientesFaltantes(@PathVariable Long id) {
        try {
            Menu registro = menuService.obtenerRegistroPorId(id);
            List<String> missingIngredients = validacionIngredienteService
                    .obtenerIngredientesNoDisponibles(registro.getIngredients());
            
            return ResponseEntity.ok(Map.of(
                "id", id,
                "description", registro.getDescription(),
                "status", registro.getStatus(),
                "missingIngredients", missingIngredients
            ));
        } catch (RuntimeException e) {
            throw e;
        }
    }

    /**
     * PUT /registros/{id}/actualizar-estado
     * Actualizar el estado del registro basado en disponibilidad actual
     */
    @PutMapping("/{id}/actualizar-estado")
    public ResponseEntity<Menu> actualizarEstado(@PathVariable Long id) {
        try {
            Menu registroActualizado = menuService.actualizarEstadoRegistro(id);
            return ResponseEntity.ok(registroActualizado);
        } catch (RuntimeException e) {
            throw e;
        }
    }

    /**
     * POST /registros/{id}/agregar-ingredients
     * Agregar ingredients existentes al registro por sus IDs
     * Body: { "ingredientesIds": [1, 2, 3] }
     */
    @PostMapping("/{id}/agregar-ingredients")
    public ResponseEntity<Menu> agregarIngredientes(
            @PathVariable Long id,
            @RequestBody Map<String, Object> request) {
        
        try {
            @SuppressWarnings("unchecked")
            List<Integer> ingredientesIds = (List<Integer>) request.get("ingredientesIds");

            if (ingredientesIds == null || ingredientesIds.isEmpty()) {
                throw new IllegalArgumentException("Se deben proporcionar ingredientesIds");
            }

            Menu registroActualizado = menuService.agregarIngredientesAlRegistro(id, ingredientesIds);
            return ResponseEntity.ok(registroActualizado);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error al agregar ingredients: " + e.getMessage(), e);
        }
    }
}

