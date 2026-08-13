package com.restaurant.cookie.service;

import com.restaurant.cookie.model.Ingrediente;
import com.restaurant.cookie.repository.IngredienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IngredienteService {

    private final IngredienteRepository ingredienteRepository;

    /**
     * Crear un nuevo ingrediente
     */
    public Ingrediente crearIngrediente(Ingrediente ingrediente) {
        validarIngrediente(ingrediente);
        return ingredienteRepository.save(ingrediente);
    }

    /**
     * Obtener todos los ingredientes
     */
    public List<Ingrediente> obtenerTodos() {
        return ingredienteRepository.findAll();
    }

    /**
     * Obtener un ingrediente por ID
     */
    public Ingrediente obtenerPorId(Long id) {
        return ingredienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ingrediente no encontrado con id: " + id));
    }

    /**
     * Actualizar un ingrediente
     */
    public Ingrediente actualizarIngrediente(Long id, Ingrediente ingredienteActualizado) {
        Ingrediente ingrediente = obtenerPorId(id);
        
        if (ingredienteActualizado.getNombre() != null && !ingredienteActualizado.getNombre().isBlank()) {
            ingrediente.setNombre(ingredienteActualizado.getNombre());
        }
        
        if (ingredienteActualizado.getCantidad() != null) {
            ingrediente.setCantidad(ingredienteActualizado.getCantidad());
        }
        
        if (ingredienteActualizado.getUnidad() != null && !ingredienteActualizado.getUnidad().isBlank()) {
            ingrediente.setUnidad(ingredienteActualizado.getUnidad());
        }
        
        validarIngrediente(ingrediente);
        return ingredienteRepository.save(ingrediente);
    }

    /**
     * Eliminar un ingrediente
     */
    public void eliminarIngrediente(Long id) {
        if (!ingredienteRepository.existsById(id)) {
            throw new RuntimeException("Ingrediente no encontrado con id: " + id);
        }
        ingredienteRepository.deleteById(id);
    }

    /**
     * Validar los datos del ingrediente
     */
    private void validarIngrediente(Ingrediente ingrediente) {
        if (ingrediente.getNombre() == null || ingrediente.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre del ingrediente es obligatorio");
        }
        
        if (ingrediente.getCantidad() == null || ingrediente.getCantidad() <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a 0");
        }
        
        if (ingrediente.getUnidad() == null || ingrediente.getUnidad().isBlank()) {
            throw new IllegalArgumentException("La unidad de medida es obligatoria");
        }
    }
}
