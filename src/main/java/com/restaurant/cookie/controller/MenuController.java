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
            String descripcion = (String) request.get("descripcion");
            Object precioRaw = request.get("precio");
            Double precio = precioRaw instanceof Number ? ((Number) precioRaw).doubleValue() : null;
            
            @SuppressWarnings("unchecked")
            List<Integer> ingredientesIds = (List<Integer>) request.get("ingredientesIds");

            if (descripcion == null || descripcion.isBlank()) {
                throw new IllegalArgumentException("La descripcion es obligatoria");
            }
            if (precio == null) {
                throw new IllegalArgumentException("El precio es obligatorio");
            }

            Menu registro = Menu.builder()
                    .descripcion(descripcion)
                    .precio(BigDecimal.valueOf(precio))
                    .build();

            Menu registroGuardado = menuService.crearRegistro(registro);
            
            // Agregar ingredientes por ID después de guardar el registro
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
     * GET /registros/{id}/ingredientes-faltantes
     * Obtener ingredientes que faltan para un registro específico
     */
    @GetMapping("/{id}/ingredientes-faltantes")
    public ResponseEntity<Map<String, Object>> obtenerIngredientesFaltantes(@PathVariable Long id) {
        try {
            Menu registro = menuService.obtenerRegistroPorId(id);
            List<String> ingredientesFaltantes = validacionIngredienteService
                    .obtenerIngredientesNoDisponibles(registro.getIngredientes());
            
            return ResponseEntity.ok(Map.of(
                "id", id,
                "descripcion", registro.getDescripcion(),
                "estado", registro.getEstado(),
                "ingredientesFaltantes", ingredientesFaltantes
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
     * POST /registros/{id}/agregar-ingredientes
     * Agregar ingredientes existentes al registro por sus IDs
     * Body: { "ingredientesIds": [1, 2, 3] }
     */
    @PostMapping("/{id}/agregar-ingredientes")
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
            throw new RuntimeException("Error al agregar ingredientes: " + e.getMessage(), e);
        }
    }
}

