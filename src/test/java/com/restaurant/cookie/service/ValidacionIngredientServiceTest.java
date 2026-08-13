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

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ValidacionIngredienteService Tests")
class ValidacionIngredientServiceTest {

    @Mock
    private IngredientRepository ingredientRepository;

    @InjectMocks
    private ValidacionIngredienteService validacionIngredienteService;

    private Ingredient chocolate;
    private Ingredient vainilla;
    private Ingredient mantequilla;

    @BeforeEach
    void setUp() {
        chocolate = Ingredient.builder()
                .id(1L)
                .name("Chocolate")
                .quantity(500.0)
                .unit("g")
                .build();

        vainilla = Ingredient.builder()
                .id(2L)
                .name("Vainilla")
                .quantity(100.0)
                .unit("ml")
                .build();

        mantequilla = Ingredient.builder()
                .id(3L)
                .name("Mantequilla")
                .quantity(250.0)
                .unit("g")
                .build();
    }

    @Test
    @DisplayName("Validar disponibilidad - Debe retornar true cuando lista es nula")
    void testValidarDisponibilidadListaNula() {
        // Act
        boolean resultado = validacionIngredienteService.validarDisponibilidadIngredientes(null);

        // Assert
        assertTrue(resultado);
        verify(ingredientRepository, never()).findAll();
    }

    @Test
    @DisplayName("Validar disponibilidad - Debe retornar true cuando lista está vacía")
    void testValidarDisponibilidadListaBacia() {
        // Act
        boolean resultado = validacionIngredienteService.validarDisponibilidadIngredientes(new ArrayList<>());

        // Assert
        assertTrue(resultado);
        verify(ingredientRepository, never()).findAll();
    }

    @Test
    @DisplayName("Validar disponibilidad - Debe retornar true cuando todos los ingredients están disponibles")
    void testValidarDisponibilidadTodosDisponibles() {
        // Arrange
        List<Ingredient> ingredientesRequeridos = List.of(chocolate, vainilla);
        List<Ingredient> ingredientesDisponibles = List.of(chocolate, vainilla, mantequilla);

        when(ingredientRepository.findAll()).thenReturn(ingredientesDisponibles);

        // Act
        boolean resultado = validacionIngredienteService.validarDisponibilidadIngredientes(ingredientesRequeridos);

        // Assert
        assertTrue(resultado);
        verify(ingredientRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Validar disponibilidad - Debe retornar false cuando falta un ingrediente")
    void testValidarDisponibilidadFaltaIngrediente() {
        // Arrange
        List<Ingredient> ingredientesRequeridos = List.of(chocolate, vainilla);
        List<Ingredient> ingredientesDisponibles = List.of(chocolate, mantequilla);

        when(ingredientRepository.findAll()).thenReturn(ingredientesDisponibles);

        // Act
        boolean resultado = validacionIngredienteService.validarDisponibilidadIngredientes(ingredientesRequeridos);

        // Assert
        assertFalse(resultado);
        verify(ingredientRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Validar disponibilidad - Debe retornar false cuando ingrediente tiene cantidad 0")
    void testValidarDisponibilidadIngredienteSinCantidad() {
        // Arrange
        Ingredient chocolateSinCantidad = Ingredient.builder()
                .id(1L)
                .name("Chocolate")
                .quantity(0.0)
                .unit("g")
                .build();

        List<Ingredient> ingredientesRequeridos = List.of(chocolate);
        List<Ingredient> ingredientesDisponibles = List.of(chocolateSinCantidad, vainilla);

        when(ingredientRepository.findAll()).thenReturn(ingredientesDisponibles);

        // Act
        boolean resultado = validacionIngredienteService.validarDisponibilidadIngredientes(ingredientesRequeridos);

        // Assert
        assertFalse(resultado);
        verify(ingredientRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Validar disponibilidad - Debe retornar false cuando ingrediente tiene cantidad nula")
    void testValidarDisponibilidadIngredienteCantidadNula() {
        // Arrange
        Ingredient chocolateCantidadNula = Ingredient.builder()
                .id(1L)
                .name("Chocolate")
                .quantity(null)
                .unit("g")
                .build();

        List<Ingredient> ingredientesRequeridos = List.of(chocolate);
        List<Ingredient> ingredientesDisponibles = List.of(chocolateCantidadNula, vainilla);

        when(ingredientRepository.findAll()).thenReturn(ingredientesDisponibles);

        // Act
        boolean resultado = validacionIngredienteService.validarDisponibilidadIngredientes(ingredientesRequeridos);

        // Assert
        assertFalse(resultado);
        verify(ingredientRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Validar disponibilidad - Debe retornar true ignorando mayúsculas y minúsculas")
    void testValidarDisponibilidadCaseInsensitive() {
        // Arrange
        Ingredient chocolateMinuscula = Ingredient.builder()
                .id(1L)
                .name("chocolate")
                .quantity(500.0)
                .unit("g")
                .build();

        Ingredient chocolateMayuscula = Ingredient.builder()
                .id(1L)
                .name("CHOCOLATE")
                .quantity(500.0)
                .unit("g")
                .build();

        List<Ingredient> ingredientesRequeridos = List.of(chocolateMinuscula);
        List<Ingredient> ingredientesDisponibles = List.of(chocolateMayuscula);

        when(ingredientRepository.findAll()).thenReturn(ingredientesDisponibles);

        // Act
        boolean resultado = validacionIngredienteService.validarDisponibilidadIngredientes(ingredientesRequeridos);

        // Assert
        assertTrue(resultado);
        verify(ingredientRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Validar disponibilidad - Debe validar múltiples ingredients")
    void testValidarDisponibilidadMultiplesIngredientes() {
        // Arrange
        List<Ingredient> ingredientesRequeridos = List.of(chocolate, vainilla, mantequilla);
        List<Ingredient> ingredientesDisponibles = List.of(chocolate, vainilla, mantequilla);

        when(ingredientRepository.findAll()).thenReturn(ingredientesDisponibles);

        // Act
        boolean resultado = validacionIngredienteService.validarDisponibilidadIngredientes(ingredientesRequeridos);

        // Assert
        assertTrue(resultado);
        verify(ingredientRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Validar disponibilidad - Debe retornar true cuando hay más ingredients disponibles de los necesarios")
    void testValidarDisponibilidadMasIngredientesDisponibles() {
        // Arrange
        Ingredient azucar = Ingredient.builder()
                .id(4L)
                .name("Azúcar")
                .quantity(1000.0)
                .unit("g")
                .build();

        Ingredient sal = Ingredient.builder()
                .id(5L)
                .name("Sal")
                .quantity(50.0)
                .unit("g")
                .build();

        List<Ingredient> ingredientesRequeridos = List.of(chocolate);
        List<Ingredient> ingredientesDisponibles = List.of(chocolate, vainilla, mantequilla, azucar, sal);

        when(ingredientRepository.findAll()).thenReturn(ingredientesDisponibles);

        // Act
        boolean resultado = validacionIngredienteService.validarDisponibilidadIngredientes(ingredientesRequeridos);

        // Assert
        assertTrue(resultado);
        verify(ingredientRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Validar disponibilidad - Debe retornar false cuando uno de muchos ingredients no está disponible")
    void testValidarDisponibilidadUnoDeMuchosNoDisponible() {
        // Arrange
        Ingredient azucar = Ingredient.builder()
                .id(4L)
                .name("Azúcar")
                .quantity(1000.0)
                .unit("g")
                .build();

        Ingredient pimienta = Ingredient.builder()
                .id(5L)
                .name("Pimienta")
                .quantity(10.0)
                .unit("g")
                .build();

        List<Ingredient> ingredientesRequeridos = List.of(chocolate, vainilla, pimienta);
        List<Ingredient> ingredientesDisponibles = List.of(chocolate, vainilla, mantequilla, azucar);

        when(ingredientRepository.findAll()).thenReturn(ingredientesDisponibles);

        // Act
        boolean resultado = validacionIngredienteService.validarDisponibilidadIngredientes(ingredientesRequeridos);

        // Assert
        assertFalse(resultado);
        verify(ingredientRepository, times(1)).findAll();
    }
}
