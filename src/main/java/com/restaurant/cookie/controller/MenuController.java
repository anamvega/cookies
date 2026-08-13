package com.restaurant.cookie.controller;

import com.restaurant.cookie.model.Menu;
import com.restaurant.cookie.model.Ingrediente;
import com.restaurant.cookie.service.MenuService;
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

    @PostMapping
    public ResponseEntity<Menu> crearNuevoPlato(@RequestBody Map<String, Object> request) {
        String descripcion = (String) request.get("descripcion");
        Object precioRaw = request.get("precio");
        Double precio = precioRaw instanceof Number ? ((Number) precioRaw).doubleValue() : null;

        @SuppressWarnings("unchecked")
        List<String> ingredientesNombres = (List<String>) request.get("ingredientes");

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

        if (ingredientesNombres != null && !ingredientesNombres.isEmpty()) {
            ingredientesNombres.forEach(nombre -> {
                if (nombre == null || nombre.isBlank()) {
                    return;
                }
                Ingrediente ingrediente = Ingrediente.builder()
                        .nombre(nombre)
                        .registro(registro)
                        .build();
                registro.getIngredientes().add(ingrediente);
            });
        }

        Menu registroGuardado = menuService.crearRegistro(registro);
        return ResponseEntity.status(HttpStatus.CREATED).body(registroGuardado);
    }

    @GetMapping
    public ResponseEntity<List<Menu>> obtenerTodos() {
        List<Menu> registros = menuService.obtenerTodosLosRegistros();
        return ResponseEntity.ok(registros);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Menu> obtenerPorId(@PathVariable Long id) {
        Menu registro = menuService.obtenerRegistroPorId(id);
        return ResponseEntity.ok(registro);
    }
}

