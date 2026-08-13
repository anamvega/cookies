package com.restaurant.cookie.service;

import com.restaurant.cookie.gateway.MenuGateway;
import com.restaurant.cookie.model.Ingredient;
import com.restaurant.cookie.model.Menu;
import com.restaurant.cookie.repository.IngredientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuGateway menuGateway;
    private final ValidacionIngredienteService validacionIngredienteService;
    private final IngredientRepository ingredientRepository;

    /**
     * Crear un nuevo registro validando disponibilidad de ingredients
     * Si algún ingrediente no está disponible, el estado se establece en 1 (no disponible)
     */
    public Menu crearRegistro(Menu registro) {
        // Validar disponibilidad de ingredients
        boolean disponible = validacionIngredienteService.validarDisponibilidadIngredientes(registro.getIngredients());
        
        // Set status based on availability
        // 0 = available, 1 = not available
        registro.setStatus(disponible ? 0 : 1);
        
        return menuGateway.save(registro);
    }

    public List<Menu> obtenerTodosLosRegistros() {
        return menuGateway.findAll();
    }

    public Menu obtenerRegistroPorId(Long id) {
        return menuGateway.findById(id)
                .orElseThrow(() -> new RuntimeException("Registro no encontrado con id: " + id));
    }

    /**
     * Obtener solo los registros disponibles (estado = 0)
     */
    public List<Menu> obtenerRegistrosDisponibles() {
        return menuGateway.findAll().stream()
                .filter(menu -> menu.getStatus() == 0)
                .toList();
    }

    /**
     * Obtener solo los registros no disponibles (estado = 1)
     */
    public List<Menu> obtenerRegistrosNoDisponibles() {
        return menuGateway.findAll().stream()
                .filter(menu -> menu.getStatus() == 1)
                .toList();
    }

    /**
     * Actualizar el estado de un registro basado en disponibilidad de ingredients
     */
    public Menu actualizarEstadoRegistro(Long id) {
        Menu registro = obtenerRegistroPorId(id);
        boolean disponible = validacionIngredienteService.validarDisponibilidadIngredientes(registro.getIngredients());
        registro.setStatus(disponible ? 0 : 1);
        return menuGateway.save(registro);
    }

    /**
     * Agregar ingredients al registro por sus IDs
     * 
     * @param registroId ID del registro (menú)
     * @param ingredientesIds Lista de IDs de ingredients a agregar
     * @return El registro actualizado con los nuevos ingredients
     */
    public Menu agregarIngredientesAlRegistro(Long registroId, List<Integer> ingredientesIds) {
        Menu registro = obtenerRegistroPorId(registroId);

        if (ingredientesIds != null && !ingredientesIds.isEmpty()) {
            for (Integer id : ingredientesIds) {
                try {
                    Ingredient ingredient = ingredientRepository.findById(id.longValue())
                            .orElseThrow(() -> new RuntimeException("Ingredient no encontrado con id: " + id));
                    
                    // Agregar el ingredient existente al registro
                    if (!registro.getIngredients().contains(ingredient)) {
                        registro.getIngredients().add(ingredient);
                    }
                } catch (RuntimeException e) {
                    throw new RuntimeException("Error al agregar ingrediente con id: " + id + " - " + e.getMessage());
                }
            }
        }

        // Recalculate status based on availability
        boolean disponible = validacionIngredienteService.validarDisponibilidadIngredientes(registro.getIngredients());
        registro.setStatus(disponible ? 0 : 1);

        return menuGateway.save(registro);
    }
}

