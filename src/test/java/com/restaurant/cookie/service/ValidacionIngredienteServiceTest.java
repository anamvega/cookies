package com.restaurant.cookie.service;

import com.restaurant.cookie.model.Ingrediente;
import com.restaurant.cookie.repository.IngredienteRepository;
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
class ValidacionIngredienteServiceTest {

    @Mock
    private IngredienteRepository ingredienteRepository;

    @InjectMocks
    private ValidacionIngredienteService validacionIngredienteService;

    private Ingrediente chocolate;
    private Ingrediente vainilla;
    private Ingrediente mantequilla;

    @BeforeEach
    void setUp() {
        chocolate = Ingrediente.builder()
                .id(1L)
                .nombre("Chocolate")
                .cantidad(500.0)
                .unidad("g")
                .build();

        vainilla = Ingrediente.builder()
                .id(2L)
                .nombre("Vainilla")
                .cantidad(100.0)
                .unidad("ml")
                .build();

        mantequilla = Ingrediente.builder()
                .id(3L)
                .nombre("Mantequilla")
                .cantidad(250.0)
                .unidad("g")
                .build();
    }

    @Test
    @DisplayName("Validar disponibilidad - Debe retornar true cuando lista es nula")
    void testValidarDisponibilidadListaNula() {
        // Act
        boolean resultado = validacionIngredienteService.validarDisponibilidadIngredientes(null);

        // Assert
        assertTrue(resultado);
        verify(ingredienteRepository, never()).findAll();
    }

    @Test
    @DisplayName("Validar disponibilidad - Debe retornar true cuando lista está vacía")
    void testValidarDisponibilidadListaBacia() {
        // Act
        boolean resultado = validacionIngredienteService.validarDisponibilidadIngredientes(new ArrayList<>());

        // Assert
        assertTrue(resultado);
        verify(ingredienteRepository, never()).findAll();
    }

    @Test
    @DisplayName("Validar disponibilidad - Debe retornar true cuando todos los ingredientes están disponibles")
    void testValidarDisponibilidadTodosDisponibles() {
        // Arrange
        List<Ingrediente> ingredientesRequeridos = List.of(chocolate, vainilla);
        List<Ingrediente> ingredientesDisponibles = List.of(chocolate, vainilla, mantequilla);

        when(ingredienteRepository.findAll()).thenReturn(ingredientesDisponibles);

        // Act
        boolean resultado = validacionIngredienteService.validarDisponibilidadIngredientes(ingredientesRequeridos);

        // Assert
        assertTrue(resultado);
        verify(ingredienteRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Validar disponibilidad - Debe retornar false cuando falta un ingrediente")
    void testValidarDisponibilidadFaltaIngrediente() {
        // Arrange
        List<Ingrediente> ingredientesRequeridos = List.of(chocolate, vainilla);
        List<Ingrediente> ingredientesDisponibles = List.of(chocolate, mantequilla);

        when(ingredienteRepository.findAll()).thenReturn(ingredientesDisponibles);

        // Act
        boolean resultado = validacionIngredienteService.validarDisponibilidadIngredientes(ingredientesRequeridos);

        // Assert
        assertFalse(resultado);
        verify(ingredienteRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Validar disponibilidad - Debe retornar false cuando ingrediente tiene cantidad 0")
    void testValidarDisponibilidadIngredienteSinCantidad() {
        // Arrange
        Ingrediente chocolateSinCantidad = Ingrediente.builder()
                .id(1L)
                .nombre("Chocolate")
                .cantidad(0.0)
                .unidad("g")
                .build();

        List<Ingrediente> ingredientesRequeridos = List.of(chocolate);
        List<Ingrediente> ingredientesDisponibles = List.of(chocolateSinCantidad, vainilla);

        when(ingredienteRepository.findAll()).thenReturn(ingredientesDisponibles);

        // Act
        boolean resultado = validacionIngredienteService.validarDisponibilidadIngredientes(ingredientesRequeridos);

        // Assert
        assertFalse(resultado);
        verify(ingredienteRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Validar disponibilidad - Debe retornar false cuando ingrediente tiene cantidad nula")
    void testValidarDisponibilidadIngredienteCantidadNula() {
        // Arrange
        Ingrediente chocolateCantidadNula = Ingrediente.builder()
                .id(1L)
                .nombre("Chocolate")
                .cantidad(null)
                .unidad("g")
                .build();

        List<Ingrediente> ingredientesRequeridos = List.of(chocolate);
        List<Ingrediente> ingredientesDisponibles = List.of(chocolateCantidadNula, vainilla);

        when(ingredienteRepository.findAll()).thenReturn(ingredientesDisponibles);

        // Act
        boolean resultado = validacionIngredienteService.validarDisponibilidadIngredientes(ingredientesRequeridos);

        // Assert
        assertFalse(resultado);
        verify(ingredienteRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Validar disponibilidad - Debe retornar true ignorando mayúsculas y minúsculas")
    void testValidarDisponibilidadCaseInsensitive() {
        // Arrange
        Ingrediente chocolateMinuscula = Ingrediente.builder()
                .id(1L)
                .nombre("chocolate")
                .cantidad(500.0)
                .unidad("g")
                .build();

        Ingrediente chocolateMayuscula = Ingrediente.builder()
                .id(1L)
                .nombre("CHOCOLATE")
                .cantidad(500.0)
                .unidad("g")
                .build();

        List<Ingrediente> ingredientesRequeridos = List.of(chocolateMinuscula);
        List<Ingrediente> ingredientesDisponibles = List.of(chocolateMayuscula);

        when(ingredienteRepository.findAll()).thenReturn(ingredientesDisponibles);

        // Act
        boolean resultado = validacionIngredienteService.validarDisponibilidadIngredientes(ingredientesRequeridos);

        // Assert
        assertTrue(resultado);
        verify(ingredienteRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Validar disponibilidad - Debe validar múltiples ingredientes")
    void testValidarDisponibilidadMultiplesIngredientes() {
        // Arrange
        List<Ingrediente> ingredientesRequeridos = List.of(chocolate, vainilla, mantequilla);
        List<Ingrediente> ingredientesDisponibles = List.of(chocolate, vainilla, mantequilla);

        when(ingredienteRepository.findAll()).thenReturn(ingredientesDisponibles);

        // Act
        boolean resultado = validacionIngredienteService.validarDisponibilidadIngredientes(ingredientesRequeridos);

        // Assert
        assertTrue(resultado);
        verify(ingredienteRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Validar disponibilidad - Debe retornar true cuando hay más ingredientes disponibles de los necesarios")
    void testValidarDisponibilidadMasIngredientesDisponibles() {
        // Arrange
        Ingrediente azucar = Ingrediente.builder()
                .id(4L)
                .nombre("Azúcar")
                .cantidad(1000.0)
                .unidad("g")
                .build();

        Ingrediente sal = Ingrediente.builder()
                .id(5L)
                .nombre("Sal")
                .cantidad(50.0)
                .unidad("g")
                .build();

        List<Ingrediente> ingredientesRequeridos = List.of(chocolate);
        List<Ingrediente> ingredientesDisponibles = List.of(chocolate, vainilla, mantequilla, azucar, sal);

        when(ingredienteRepository.findAll()).thenReturn(ingredientesDisponibles);

        // Act
        boolean resultado = validacionIngredienteService.validarDisponibilidadIngredientes(ingredientesRequeridos);

        // Assert
        assertTrue(resultado);
        verify(ingredienteRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Validar disponibilidad - Debe retornar false cuando uno de muchos ingredientes no está disponible")
    void testValidarDisponibilidadUnoDeMuchosNoDisponible() {
        // Arrange
        Ingrediente azucar = Ingrediente.builder()
                .id(4L)
                .nombre("Azúcar")
                .cantidad(1000.0)
                .unidad("g")
                .build();

        Ingrediente pimienta = Ingrediente.builder()
                .id(5L)
                .nombre("Pimienta")
                .cantidad(10.0)
                .unidad("g")
                .build();

        List<Ingrediente> ingredientesRequeridos = List.of(chocolate, vainilla, pimienta);
        List<Ingrediente> ingredientesDisponibles = List.of(chocolate, vainilla, mantequilla, azucar);

        when(ingredienteRepository.findAll()).thenReturn(ingredientesDisponibles);

        // Act
        boolean resultado = validacionIngredienteService.validarDisponibilidadIngredientes(ingredientesRequeridos);

        // Assert
        assertFalse(resultado);
        verify(ingredienteRepository, times(1)).findAll();
    }
}
