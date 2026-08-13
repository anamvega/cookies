package com.restaurant.cookie.service;

import com.restaurant.cookie.gateway.RegistroGateway;
import com.restaurant.cookie.model.Menu;
import com.restaurant.cookie.model.Ingrediente;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RegistroService {

    private final RegistroGateway registroGateway;

    public Menu crearRegistro(Menu registro) {
        // Establecer la relación inversa para los ingredientes
        if (registro.getIngredientes() != null) {
            registro.getIngredientes().forEach(ingrediente -> ingrediente.setRegistro(registro));
        }
        return registroGateway.save(registro);
    }

    public List<Menu> obtenerTodosLosRegistros() {
        return registroGateway.findAll();
    }

    public Menu obtenerRegistroPorId(Long id) {
        return registroGateway.findById(id)
                .orElseThrow(() -> new RuntimeException("Registro no encontrado con id: " + id));
    }
}

