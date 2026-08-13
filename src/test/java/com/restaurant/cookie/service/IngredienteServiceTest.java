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

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("IngredienteService Tests")
class IngredienteServiceTest {

    @Mock
    private IngredienteRepository ingredienteRepository;

    @InjectMocks
    private IngredienteService ingredienteService;

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
    @DisplayName("Crear ingrediente - Debe crear un ingrediente válido")
    void testCrearIngredienteValido() {
        // Arrange
        Ingrediente nuevoIngrediente = Ingrediente.builder()
                .nombre("Chocolate")
                .cantidad(500.0)
                .unidad("g")
                .build();

        when(ingredienteRepository.save(any(Ingrediente.class))).thenReturn(ingrediente);

        // Act
        Ingrediente resultado = ingredienteService.crearIngrediente(nuevoIngrediente);

        // Assert
        assertNotNull(resultado);
        assertEquals("Chocolate", resultado.getNombre());
        assertEquals(500.0, resultado.getCantidad());
        assertEquals("g", resultado.getUnidad());
        verify(ingredienteRepository, times(1)).save(any(Ingrediente.class));
    }

    @Test
    @DisplayName("Crear ingrediente - Debe lanzar excepción si nombre es nulo")
    void testCrearIngredienteSinNombre() {
        // Arrange
        Ingrediente ingredienteSinNombre = Ingrediente.builder()
                .nombre(null)
                .cantidad(500.0)
                .unidad("g")
                .build();

        // Act & Assert
        assertThrows(IllegalArgumentException.class, 
            () -> ingredienteService.crearIngrediente(ingredienteSinNombre));
        verify(ingredienteRepository, never()).save(any(Ingrediente.class));
    }

    @Test
    @DisplayName("Crear ingrediente - Debe lanzar excepción si nombre está en blanco")
    void testCrearIngredienteNombreEnBlanco() {
        // Arrange
        Ingrediente ingredienteBlanco = Ingrediente.builder()
                .nombre("   ")
                .cantidad(500.0)
                .unidad("g")
                .build();

        // Act & Assert
        assertThrows(IllegalArgumentException.class, 
            () -> ingredienteService.crearIngrediente(ingredienteBlanco));
        verify(ingredienteRepository, never()).save(any(Ingrediente.class));
    }

    @Test
    @DisplayName("Crear ingrediente - Debe lanzar excepción si cantidad es nula")
    void testCrearIngredienteSinCantidad() {
        // Arrange
        Ingrediente ingredienteSinCantidad = Ingrediente.builder()
                .nombre("Chocolate")
                .cantidad(null)
                .unidad("g")
                .build();

        // Act & Assert
        assertThrows(IllegalArgumentException.class, 
            () -> ingredienteService.crearIngrediente(ingredienteSinCantidad));
        verify(ingredienteRepository, never()).save(any(Ingrediente.class));
    }

    @Test
    @DisplayName("Crear ingrediente - Debe lanzar excepción si cantidad es menor o igual a 0")
    void testCrearIngredienteCantidadInvalida() {
        // Arrange
        Ingrediente ingredienteCantidadInvalida = Ingrediente.builder()
                .nombre("Chocolate")
                .cantidad(0.0)
                .unidad("g")
                .build();

        // Act & Assert
        assertThrows(IllegalArgumentException.class, 
            () -> ingredienteService.crearIngrediente(ingredienteCantidadInvalida));
        verify(ingredienteRepository, never()).save(any(Ingrediente.class));
    }

    @Test
    @DisplayName("Crear ingrediente - Debe lanzar excepción si unidad es nula")
    void testCrearIngredienteSinUnidad() {
        // Arrange
        Ingrediente ingredienteSinUnidad = Ingrediente.builder()
                .nombre("Chocolate")
                .cantidad(500.0)
                .unidad(null)
                .build();

        // Act & Assert
        assertThrows(IllegalArgumentException.class, 
            () -> ingredienteService.crearIngrediente(ingredienteSinUnidad));
        verify(ingredienteRepository, never()).save(any(Ingrediente.class));
    }

    @Test
    @DisplayName("Crear ingrediente - Debe lanzar excepción si unidad está en blanco")
    void testCrearIngredienteUnidadEnBlanco() {
        // Arrange
        Ingrediente ingredienteUnidadBlanca = Ingrediente.builder()
                .nombre("Chocolate")
                .cantidad(500.0)
                .unidad("   ")
                .build();

        // Act & Assert
        assertThrows(IllegalArgumentException.class, 
            () -> ingredienteService.crearIngrediente(ingredienteUnidadBlanca));
        verify(ingredienteRepository, never()).save(any(Ingrediente.class));
    }

    @Test
    @DisplayName("Obtener todos los ingredientes - Debe retornar lista de ingredientes")
    void testObtenerTodosLosIngredientes() {
        // Arrange
        List<Ingrediente> ingredientes = List.of(ingrediente);
        when(ingredienteRepository.findAll()).thenReturn(ingredientes);

        // Act
        List<Ingrediente> resultado = ingredienteService.obtenerTodos();

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Chocolate", resultado.get(0).getNombre());
        verify(ingredienteRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Obtener todos - Debe retornar lista vacía cuando no hay ingredientes")
    void testObtenerTodosVacio() {
        // Arrange
        when(ingredienteRepository.findAll()).thenReturn(List.of());

        // Act
        List<Ingrediente> resultado = ingredienteService.obtenerTodos();

        // Assert
        assertNotNull(resultado);
        assertEquals(0, resultado.size());
        verify(ingredienteRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Obtener por ID - Debe retornar el ingrediente encontrado")
    void testObtenerPorId() {
        // Arrange
        when(ingredienteRepository.findById(1L)).thenReturn(Optional.of(ingrediente));

        // Act
        Ingrediente resultado = ingredienteService.obtenerPorId(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Chocolate", resultado.getNombre());
        verify(ingredienteRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Obtener por ID - Debe lanzar excepción cuando no existe")
    void testObtenerPorIdNoEncontrado() {
        // Arrange
        when(ingredienteRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> ingredienteService.obtenerPorId(999L));
        verify(ingredienteRepository, times(1)).findById(999L);
    }

    @Test
    @DisplayName("Actualizar ingrediente - Debe actualizar todos los campos")
    void testActualizarIngrediente() {
        // Arrange
        Ingrediente ingredienteActualizado = Ingrediente.builder()
                .nombre("Chocolate Blanco")
                .cantidad(750.0)
                .unidad("g")
                .build();

        when(ingredienteRepository.findById(1L)).thenReturn(Optional.of(ingrediente));
        when(ingredienteRepository.save(any(Ingrediente.class))).thenReturn(
            Ingrediente.builder()
                .id(1L)
                .nombre("Chocolate Blanco")
                .cantidad(750.0)
                .unidad("g")
                .build()
        );

        // Act
        Ingrediente resultado = ingredienteService.actualizarIngrediente(1L, ingredienteActualizado);

        // Assert
        assertNotNull(resultado);
        assertEquals("Chocolate Blanco", resultado.getNombre());
        assertEquals(750.0, resultado.getCantidad());
        assertEquals("g", resultado.getUnidad());
        verify(ingredienteRepository, times(1)).findById(1L);
        verify(ingredienteRepository, times(1)).save(any(Ingrediente.class));
    }

    @Test
    @DisplayName("Actualizar ingrediente - Debe actualizar solo el nombre")
    void testActualizarIngredienteSoloNombre() {
        // Arrange
        Ingrediente ingredienteConNombreNuevo = Ingrediente.builder()
                .nombre("Chocolate Premium")
                .cantidad(null)
                .unidad(null)
                .build();

        Ingrediente ingredienteEsperado = Ingrediente.builder()
                .id(1L)
                .nombre("Chocolate Premium")
                .cantidad(500.0)
                .unidad("g")
                .build();

        when(ingredienteRepository.findById(1L)).thenReturn(Optional.of(ingrediente));
        when(ingredienteRepository.save(any(Ingrediente.class))).thenReturn(ingredienteEsperado);

        // Act
        Ingrediente resultado = ingredienteService.actualizarIngrediente(1L, ingredienteConNombreNuevo);

        // Assert
        assertNotNull(resultado);
        assertEquals("Chocolate Premium", resultado.getNombre());
        assertEquals(500.0, resultado.getCantidad());
        verify(ingredienteRepository, times(1)).save(any(Ingrediente.class));
    }

    @Test
    @DisplayName("Actualizar ingrediente - Debe lanzar excepción si nueva cantidad es inválida")
    void testActualizarIngredienteCantidadInvalida() {
        // Arrange
        Ingrediente ingredienteCantidadInvalida = Ingrediente.builder()
                .nombre("Chocolate Premium")
                .cantidad(0.0)
                .unidad("g")
                .build();

        when(ingredienteRepository.findById(1L)).thenReturn(Optional.of(ingrediente));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, 
            () -> ingredienteService.actualizarIngrediente(1L, ingredienteCantidadInvalida));
    }

    @Test
    @DisplayName("Eliminar ingrediente - Debe eliminar el ingrediente")
    void testEliminarIngrediente() {
        // Arrange
        when(ingredienteRepository.existsById(1L)).thenReturn(true);

        // Act
        ingredienteService.eliminarIngrediente(1L);

        // Assert
        verify(ingredienteRepository, times(1)).existsById(1L);
        verify(ingredienteRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Eliminar ingrediente - Debe lanzar excepción si no existe")
    void testEliminarIngredienteNoExistente() {
        // Arrange
        when(ingredienteRepository.existsById(999L)).thenReturn(false);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> ingredienteService.eliminarIngrediente(999L));
        verify(ingredienteRepository, never()).deleteById(any());
    }
}
