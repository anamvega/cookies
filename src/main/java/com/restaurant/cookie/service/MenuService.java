package com.restaurant.cookie.service;

import com.restaurant.cookie.gateway.MenuGateway;
import com.restaurant.cookie.model.Menu;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuGateway menuGateway;

    public Menu crearRegistro(Menu registro) {
        // Establecer la relación inversa para los ingredientes
        if (registro.getIngredientes() != null) {
            registro.getIngredientes().forEach(ingrediente -> ingrediente.setRegistro(registro));
        }
        return menuGateway.save(registro);
    }

    public List<Menu> obtenerTodosLosRegistros() {
        return menuGateway.findAll();
    }

    public Menu obtenerRegistroPorId(Long id) {
        return menuGateway.findById(id)
                .orElseThrow(() -> new RuntimeException("Registro no encontrado con id: " + id));
    }
}

