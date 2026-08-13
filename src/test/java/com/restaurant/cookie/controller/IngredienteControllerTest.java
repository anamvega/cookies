package com.restaurant.cookie.controller;

import com.restaurant.cookie.model.Ingrediente;
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
class IngredienteControllerTest {

    @Mock
    private IngredienteService ingredienteService;

    @InjectMocks
    private IngredienteController ingredienteController;

    private Ingrediente ingrediente;

    @BeforeEach
    void setUp() {
        ingrediente = Ingrediente.builder()
                .id(1L)
                .nombre("Chocolate")
                .cantidad(500.0)
                .unidad("g")
                .build();
    }

    @Test
    @DisplayName("POST - Debe crear un ingrediente válido")
    void testCrearIngredienteValido() {
        // Arrange
        when(ingredienteService.crearIngrediente(any(Ingrediente.class))).thenReturn(ingrediente);

        Map<String, Object> request = Map.of(
            "nombre", "Chocolate",
            "cantidad", 500.0,
            "unidad", "g"
        );

        // Act
        ResponseEntity<Ingrediente> resultado = ingredienteController.crearIngrediente(request);

        // Assert
        assertNotNull(resultado);
        assertEquals(HttpStatus.CREATED, resultado.getStatusCode());
        assertEquals("Chocolate", resultado.getBody().getNombre());
        verify(ingredienteService, times(1)).crearIngrediente(any(Ingrediente.class));
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
            ingredienteController.crearIngrediente(request)
        );
        verify(ingredienteService, never()).crearIngrediente(any());
    }

    @Test
    @DisplayName("GET - Debe retornar lista de ingredientes")
    void testObtenerTodos() {
        // Arrange
        List<Ingrediente> ingredientes = List.of(ingrediente);
        when(ingredienteService.obtenerTodos()).thenReturn(ingredientes);

        // Act
        ResponseEntity<List<Ingrediente>> resultado = ingredienteController.obtenerTodos();

        // Assert
        assertNotNull(resultado);
        assertEquals(HttpStatus.OK, resultado.getStatusCode());
        assertEquals(1, resultado.getBody().size());
        verify(ingredienteService, times(1)).obtenerTodos();
    }

    @Test
    @DisplayName("GET /{id} - Debe retornar ingrediente por ID")
    void testObtenerPorId() {
        // Arrange
        when(ingredienteService.obtenerPorId(1L)).thenReturn(ingrediente);

        // Act
        ResponseEntity<Ingrediente> resultado = ingredienteController.obtenerPorId(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals(HttpStatus.OK, resultado.getStatusCode());
        assertEquals("Chocolate", resultado.getBody().getNombre());
        verify(ingredienteService, times(1)).obtenerPorId(1L);
    }

    @Test
    @DisplayName("PUT /{id} - Debe actualizar ingrediente")
    void testActualizarIngrediente() {
        // Arrange
        Ingrediente actualizado = Ingrediente.builder()
                .id(1L)
                .nombre("Chocolate Blanco")
                .cantidad(750.0)
                .unidad("g")
                .build();

        when(ingredienteService.actualizarIngrediente(eq(1L), any(Ingrediente.class)))
                .thenReturn(actualizado);

        Map<String, Object> request = Map.of(
            "nombre", "Chocolate Blanco",
            "cantidad", 750.0,
            "unidad", "g"
        );

        // Act
        ResponseEntity<Ingrediente> resultado = ingredienteController.actualizarIngrediente(1L, request);

        // Assert
        assertNotNull(resultado);
        assertEquals(HttpStatus.OK, resultado.getStatusCode());
        assertEquals("Chocolate Blanco", resultado.getBody().getNombre());
        verify(ingredienteService, times(1)).actualizarIngrediente(eq(1L), any());
    }

    @Test
    @DisplayName("DELETE /{id} - Debe eliminar ingrediente")
    void testEliminarIngrediente() {
        // Arrange
        doNothing().when(ingredienteService).eliminarIngrediente(1L);

        // Act
        ResponseEntity<Void> resultado = ingredienteController.eliminarIngrediente(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals(HttpStatus.NO_CONTENT, resultado.getStatusCode());
        verify(ingredienteService, times(1)).eliminarIngrediente(1L);
    }
}
