package com.example.cookie.controller;

import com.example.cookie.model.Registro;
import com.example.cookie.service.RegistroService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/registros")
@RequiredArgsConstructor
public class RegistroController {

    private final RegistroService registroService;

    @PostMapping
    public ResponseEntity<Registro> crearRegistro(@RequestBody Map<String, Object> request) {
        String descripcion = (String) request.get("descripcion");
        Double precio = (Double) request.get("precio");

        Registro registro = Registro.builder()
                .descripcion(descripcion)
                .precio(java.math.BigDecimal.valueOf(precio))
                .build();

        Registro registroGuardado = registroService.crearRegistro(registro);
        return ResponseEntity.status(HttpStatus.CREATED).body(registroGuardado);
    }

    @GetMapping
    public ResponseEntity<List<Registro>> obtenerTodos() {
        List<Registro> registros = registroService.obtenerTodosLosRegistros();
        return ResponseEntity.ok(registros);
    }
}
