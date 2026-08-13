package com.restaurant.cookie.controller;

import com.restaurant.cookie.model.Ingredient;
import com.restaurant.cookie.service.IngredienteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("IngredienteController Tests")
class IngredientControllerTest {

    @Mock
    private IngredienteService ingredienteService;

    @InjectMocks
    private IngredientController ingredientController;

    private Ingredient ingredient;

    @BeforeEach
    void setUp() {
        ingredient = Ingredient.builder()
                .id(1L)
                .name("Chocolate")
                .quantity(500.0)
                .unit("g")
                .build();
    }

    @Test
    @DisplayName("POST - Debe crear un ingredient válido")
    void testCrearIngredienteValido() {
        // Arrange
        when(ingredienteService.crearIngrediente(any(Ingredient.class))).thenReturn(ingredient);

        Map<String, Object> request = Map.of(
            "name", "Chocolate",
            "quantity", 500.0,
            "unit", "g"
        );

        // Act
        ResponseEntity<Ingredient> result = ingredientController.crearIngrediente(request);

        // Assert
        assertNotNull(result);
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals("Chocolate", result.getBody().getName());
        verify(ingredienteService, times(1)).crearIngrediente(any(Ingredient.class));
    }

    @Test
    @DisplayName("POST - Debe lanzar excepción si nombre es nulo")
    void testCrearIngredienteSinNombre() {
        // Arrange
        Map<String, Object> request = Map.of(
            "cantidad", 500.0,
            "unidad", "g"
        );

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> 
            ingredientController.crearIngrediente(request)
        );
        verify(ingredienteService, never()).crearIngrediente(any());
    }

    @Test
    @DisplayName("GET - Debe retornar lista de ingredients")
    void testObtenerTodos() {
        // Arrange
        List<Ingredient> ingredients = List.of(ingredient);
        when(ingredienteService.obtenerTodos()).thenReturn(ingredients);

        // Act
        ResponseEntity<List<Ingredient>> resultado = ingredientController.obtenerTodos();

        // Assert
        assertNotNull(resultado);
        assertEquals(HttpStatus.OK, resultado.getStatusCode());
        assertEquals(1, resultado.getBody().size());
        verify(ingredienteService, times(1)).obtenerTodos();
    }

    @Test
    @DisplayName("GET /{id} - Debe retornar ingredient por ID")
    void testObtenerPorId() {
        // Arrange
        when(ingredienteService.obtenerPorId(1L)).thenReturn(ingredient);

        // Act
        ResponseEntity<Ingredient> result = ingredientController.obtenerPorId(1L);

        // Assert
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("Chocolate", result.getBody().getName());
        verify(ingredienteService, times(1)).obtenerPorId(1L);
    }

    @Test
    @DisplayName("PUT /{id} - Debe actualizar ingredient")
    void testActualizarIngrediente() {
        // Arrange
        Ingredient updated = Ingredient.builder()
                .id(1L)
                .name("White Chocolate")
                .quantity(750.0)
                .unit("g")
                .build();

        when(ingredienteService.actualizarIngrediente(eq(1L), any(Ingredient.class)))
                .thenReturn(updated);

        Map<String, Object> request = Map.of(
            "name", "White Chocolate",
            "quantity", 750.0,
            "unit", "g"
        );

        // Act
        ResponseEntity<Ingredient> result = ingredientController.actualizarIngrediente(1L, request);

        // Assert
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("White Chocolate", result.getBody().getName());
        verify(ingredienteService, times(1)).actualizarIngrediente(eq(1L), any());
    }

    @Test
    @DisplayName("DELETE /{id} - Debe eliminar ingredient")
    void testEliminarIngrediente() {
        // Arrange
        doNothing().when(ingredienteService).eliminarIngrediente(1L);

        // Act
        ResponseEntity<Void> resultado = ingredientController.eliminarIngrediente(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals(HttpStatus.NO_CONTENT, resultado.getStatusCode());
        verify(ingredienteService, times(1)).eliminarIngrediente(1L);
    }
}
