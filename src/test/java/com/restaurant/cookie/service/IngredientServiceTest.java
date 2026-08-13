package com.restaurant.cookie.service;

import com.restaurant.cookie.model.Ingredient;
import com.restaurant.cookie.repository.IngredientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("IngredienteService Tests")
class IngredientServiceTest {

    @Mock
    private IngredientRepository ingredientRepository;

    @InjectMocks
    private IngredienteService ingredienteService;

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
    @DisplayName("Crear ingredient - Debe crear un ingredient válido")
    void testCrearIngredienteValido() {
        // Arrange
        Ingredient nuevoIngredient = Ingredient.builder()
                .name("Chocolate")
                .quantity(500.0)
                .unit("g")
                .build();

        when(ingredientRepository.save(any(Ingredient.class))).thenReturn(ingredient);

        // Act
        Ingredient resultado = ingredienteService.crearIngrediente(nuevoIngredient);

        // Assert
        assertNotNull(resultado);
        assertEquals("Chocolate", resultado.getName());
        assertEquals(500.0, resultado.getQuantity());
        assertEquals("g", resultado.getUnit());
        verify(ingredientRepository, times(1)).save(any(Ingredient.class));
    }

    @Test
    @DisplayName("Crear ingredient - Debe lanzar excepción si nombre es nulo")
    void testCrearIngredienteSinNombre() {
        // Arrange
        Ingredient ingredientSinNombre = Ingredient.builder()
                .name(null)
                .quantity(500.0)
                .unit("g")
                .build();

        // Act & Assert
        assertThrows(IllegalArgumentException.class, 
            () -> ingredienteService.crearIngrediente(ingredientSinNombre));
        verify(ingredientRepository, never()).save(any(Ingredient.class));
    }

    @Test
    @DisplayName("Crear ingredient - Debe lanzar excepción si nombre está en blanco")
    void testCrearIngredienteNombreEnBlanco() {
        // Arrange
        Ingredient ingredientBlanco = Ingredient.builder()
                .name("   ")
                .quantity(500.0)
                .unit("g")
                .build();

        // Act & Assert
        assertThrows(IllegalArgumentException.class, 
            () -> ingredienteService.crearIngrediente(ingredientBlanco));
        verify(ingredientRepository, never()).save(any(Ingredient.class));
    }

    @Test
    @DisplayName("Crear ingredient - Debe lanzar excepción si cantidad es nula")
    void testCrearIngredienteSinCantidad() {
        // Arrange
        Ingredient ingredientSinCantidad = Ingredient.builder()
                .name("Chocolate")
                .quantity(null)
                .unit("g")
                .build();

        // Act & Assert
        assertThrows(IllegalArgumentException.class, 
            () -> ingredienteService.crearIngrediente(ingredientSinCantidad));
        verify(ingredientRepository, never()).save(any(Ingredient.class));
    }

    @Test
    @DisplayName("Crear ingredient - Debe lanzar excepción si cantidad es menor o igual a 0")
    void testCrearIngredienteCantidadInvalida() {
        // Arrange
        Ingredient ingredientCantidadInvalida = Ingredient.builder()
                .name("Chocolate")
                .quantity(0.0)
                .unit("g")
                .build();

        // Act & Assert
        assertThrows(IllegalArgumentException.class, 
            () -> ingredienteService.crearIngrediente(ingredientCantidadInvalida));
        verify(ingredientRepository, never()).save(any(Ingredient.class));
    }

    @Test
    @DisplayName("Crear ingredient - Debe lanzar excepción si unidad es nula")
    void testCrearIngredienteSinUnidad() {
        // Arrange
        Ingredient ingredientSinUnidad = Ingredient.builder()
                .name("Chocolate")
                .quantity(500.0)
                .unit(null)
                .build();

        // Act & Assert
        assertThrows(IllegalArgumentException.class, 
            () -> ingredienteService.crearIngrediente(ingredientSinUnidad));
        verify(ingredientRepository, never()).save(any(Ingredient.class));
    }

    @Test
    @DisplayName("Crear ingredient - Debe lanzar excepción si unidad está en blanco")
    void testCrearIngredienteUnidadEnBlanco() {
        // Arrange
        Ingredient ingredientUnidadBlanca = Ingredient.builder()
                .name("Chocolate")
                .quantity(500.0)
                .unit("   ")
                .build();

        // Act & Assert
        assertThrows(IllegalArgumentException.class, 
            () -> ingredienteService.crearIngrediente(ingredientUnidadBlanca));
        verify(ingredientRepository, never()).save(any(Ingredient.class));
    }

    @Test
    @DisplayName("Obtener todos los ingredients - Debe retornar lista de ingredients")
    void testObtenerTodosLosIngredientes() {
        // Arrange
        List<Ingredient> ingredients = List.of(ingredient);
        when(ingredientRepository.findAll()).thenReturn(ingredients);

        // Act
        List<Ingredient> resultado = ingredienteService.obtenerTodos();

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Chocolate", resultado.get(0).getName());
        verify(ingredientRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Obtener todos - Debe retornar lista vacía cuando no hay ingredients")
    void testObtenerTodosVacio() {
        // Arrange
        when(ingredientRepository.findAll()).thenReturn(List.of());

        // Act
        List<Ingredient> resultado = ingredienteService.obtenerTodos();

        // Assert
        assertNotNull(resultado);
        assertEquals(0, resultado.size());
        verify(ingredientRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Obtener por ID - Debe retornar el ingredient encontrado")
    void testObtenerPorId() {
        // Arrange
        when(ingredientRepository.findById(1L)).thenReturn(Optional.of(ingredient));

        // Act
        Ingredient resultado = ingredienteService.obtenerPorId(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Chocolate", resultado.getName());
        verify(ingredientRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Obtener por ID - Debe lanzar excepción cuando no existe")
    void testObtenerPorIdNoEncontrado() {
        // Arrange
        when(ingredientRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> ingredienteService.obtenerPorId(999L));
        verify(ingredientRepository, times(1)).findById(999L);
    }

    @Test
    @DisplayName("Actualizar ingredient - Debe actualizar todos los campos")
    void testActualizarIngrediente() {
        // Arrange
        Ingredient ingredientActualizado = Ingredient.builder()
                .name("Chocolate Blanco")
                .quantity(750.0)
                .unit("g")
                .build();

        when(ingredientRepository.findById(1L)).thenReturn(Optional.of(ingredient));
        when(ingredientRepository.save(any(Ingredient.class))).thenReturn(
            Ingredient.builder()
                .id(1L)
                .name("Chocolate Blanco")
                .quantity(750.0)
                .unit("g")
                .build()
        );

        // Act
        Ingredient resultado = ingredienteService.actualizarIngrediente(1L, ingredientActualizado);

        // Assert
        assertNotNull(resultado);
        assertEquals("Chocolate Blanco", resultado.getName());
        assertEquals(750.0, resultado.getQuantity());
        assertEquals("g", resultado.getUnit());
        verify(ingredientRepository, times(1)).findById(1L);
        verify(ingredientRepository, times(1)).save(any(Ingredient.class));
    }

    @Test
    @DisplayName("Actualizar ingredient - Debe actualizar solo el nombre")
    void testActualizarIngredienteSoloNombre() {
        // Arrange
        Ingredient ingredientConNombreNuevo = Ingredient.builder()
                .name("Chocolate Premium")
                .quantity(null)
                .unit(null)
                .build();

        Ingredient ingredientEsperado = Ingredient.builder()
                .id(1L)
                .name("Chocolate Premium")
                .quantity(500.0)
                .unit("g")
                .build();

        when(ingredientRepository.findById(1L)).thenReturn(Optional.of(ingredient));
        when(ingredientRepository.save(any(Ingredient.class))).thenReturn(ingredientEsperado);

        // Act
        Ingredient resultado = ingredienteService.actualizarIngrediente(1L, ingredientConNombreNuevo);

        // Assert
        assertNotNull(resultado);
        assertEquals("Chocolate Premium", resultado.getName());
        assertEquals(500.0, resultado.getQuantity());
        verify(ingredientRepository, times(1)).save(any(Ingredient.class));
    }

    @Test
    @DisplayName("Actualizar ingredient - Debe lanzar excepción si nueva cantidad es inválida")
    void testActualizarIngredienteCantidadInvalida() {
        // Arrange
        Ingredient ingredientCantidadInvalida = Ingredient.builder()
                .name("Chocolate Premium")
                .quantity(0.0)
                .unit("g")
                .build();

        when(ingredientRepository.findById(1L)).thenReturn(Optional.of(ingredient));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, 
            () -> ingredienteService.actualizarIngrediente(1L, ingredientCantidadInvalida));
    }

    @Test
    @DisplayName("Eliminar ingredient - Debe eliminar el ingredient")
    void testEliminarIngrediente() {
        // Arrange
        when(ingredientRepository.existsById(1L)).thenReturn(true);

        // Act
        ingredienteService.eliminarIngrediente(1L);

        // Assert
        verify(ingredientRepository, times(1)).existsById(1L);
        verify(ingredientRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Eliminar ingredient - Debe lanzar excepción si no existe")
    void testEliminarIngredienteNoExistente() {
        // Arrange
        when(ingredientRepository.existsById(999L)).thenReturn(false);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> ingredienteService.eliminarIngrediente(999L));
        verify(ingredientRepository, never()).deleteById(any());
    }
}
