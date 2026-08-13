package com.restaurant.cookie.service;

import com.restaurant.cookie.gateway.MenuGateway;
import com.restaurant.cookie.model.Menu;
import com.restaurant.cookie.model.Ingrediente;
import com.restaurant.cookie.repository.IngredienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuGateway menuGateway;
    private final ValidacionIngredienteService validacionIngredienteService;
    private final IngredienteRepository ingredienteRepository;

    /**
     * Crear un nuevo registro validando disponibilidad de ingredientes
     * Si algún ingrediente no está disponible, el estado se establece en 1 (no disponible)
     */
    public Menu crearRegistro(Menu registro) {
        // Validar disponibilidad de ingredientes
        boolean disponible = validacionIngredienteService.validarDisponibilidadIngredientes(registro.getIngredientes());
        
        // Establecer el estado basado en la disponibilidad
        // 0 = disponible, 1 = no disponible
        registro.setEstado(disponible ? 0 : 1);
        
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
                .filter(menu -> menu.getEstado() == 0)
                .toList();
    }

    /**
     * Obtener solo los registros no disponibles (estado = 1)
     */
    public List<Menu> obtenerRegistrosNoDisponibles() {
        return menuGateway.findAll().stream()
                .filter(menu -> menu.getEstado() == 1)
                .toList();
    }

    /**
     * Actualizar el estado de un registro basado en disponibilidad de ingredientes
     */
    public Menu actualizarEstadoRegistro(Long id) {
        Menu registro = obtenerRegistroPorId(id);
        boolean disponible = validacionIngredienteService.validarDisponibilidadIngredientes(registro.getIngredientes());
        registro.setEstado(disponible ? 0 : 1);
        return menuGateway.save(registro);
    }

    /**
     * Agregar ingredientes al registro por sus IDs
     * 
     * @param registroId ID del registro (menú)
     * @param ingredientesIds Lista de IDs de ingredientes a agregar
     * @return El registro actualizado con los nuevos ingredientes
     */
    public Menu agregarIngredientesAlRegistro(Long registroId, List<Integer> ingredientesIds) {
        Menu registro = obtenerRegistroPorId(registroId);

        if (ingredientesIds != null && !ingredientesIds.isEmpty()) {
            for (Integer id : ingredientesIds) {
                try {
                    Ingrediente ingrediente = ingredienteRepository.findById(id.longValue())
                            .orElseThrow(() -> new RuntimeException("Ingrediente no encontrado con id: " + id));
                    
                    // Agregar el ingrediente existente al registro
                    if (!registro.getIngredientes().contains(ingrediente)) {
                        registro.getIngredientes().add(ingrediente);
                    }
                } catch (RuntimeException e) {
                    throw new RuntimeException("Error al agregar ingrediente con id: " + id + " - " + e.getMessage());
                }
            }
        }

        // Recalcular el estado basado en la disponibilidad
        boolean disponible = validacionIngredienteService.validarDisponibilidadIngredientes(registro.getIngredientes());
        registro.setEstado(disponible ? 0 : 1);

        return menuGateway.save(registro);
    }
}

