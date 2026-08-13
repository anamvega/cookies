package com.restaurant.cookie.controller;

import com.restaurant.cookie.model.Registro;
import com.restaurant.cookie.model.Ingrediente;
import com.restaurant.cookie.service.RegistroService;
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
public class RegistroController {

    private final RegistroService registroService;

    @PostMapping
    public ResponseEntity<Registro> crearNuevoPlato(@RequestBody Map<String, Object> request) {
        String descripcion = (String) request.get("descripcion");
        Double precio = (Double) request.get("precio");
        
        @SuppressWarnings("unchecked")
        List<String> ingredientesNombres = (List<String>) request.get("ingredientes");

        Registro registro = Registro.builder()
                .descripcion(descripcion)
                .precio(BigDecimal.valueOf(precio))
                .build();
        
        // Crear y asociar ingredientes
        if (ingredientesNombres != null && !ingredientesNombres.isEmpty()) {
            ingredientesNombres.forEach(nombre -> {
                Ingrediente ingrediente = Ingrediente.builder()
                        .nombre(nombre)
                        .registro(registro)
                        .build();
                registro.getIngredientes().add(ingrediente);
            });
        }

        Registro registroGuardado = registroService.crearRegistro(registro);
        return ResponseEntity.status(HttpStatus.CREATED).body(registroGuardado);
    }

    @GetMapping
    public ResponseEntity<List<Registro>> obtenerTodos() {
        List<Registro> registros = registroService.obtenerTodosLosRegistros();
        return ResponseEntity.ok(registros);
    }
}

