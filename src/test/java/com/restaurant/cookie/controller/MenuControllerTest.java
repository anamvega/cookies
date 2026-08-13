package com.restaurant.cookie.controller;

import com.restaurant.cookie.model.Ingrediente;
import com.restaurant.cookie.model.Menu;
import com.restaurant.cookie.service.MenuService;
import com.restaurant.cookie.service.ValidacionIngredienteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("MenuController Tests")
class MenuControllerTest {

    @Mock
    private MenuService menuService;

    @Mock
    private ValidacionIngredienteService validacionIngredienteService;

    @InjectMocks
    private MenuController menuController;

    private Menu menu;
    private Ingrediente ingrediente;

    @BeforeEach
    void setUp() {
        ingrediente = Ingrediente.builder()
                .id(1L)
                .nombre("Chocolate")
                .cantidad(500.0)
                .unidad("g")
                .build();

        menu = Menu.builder()
                .id(1L)
                .descripcion("Cookie de Chocolate")
                .precio(BigDecimal.valueOf(29.99))
                .estado(0)
                .ingredientes(new ArrayList<>(List.of(ingrediente)))
                .build();
    }

    @Test
    @DisplayName("POST - Debe crear un nuevo menú")
    void testCrearNuevoPlato() {
        // Arrange
        when(menuService.crearRegistro(any(Menu.class))).thenReturn(menu);
        when(menuService.agregarIngredientesAlRegistro(1L, List.of(1)))
                .thenReturn(menu);

        Map<String, Object> request = Map.of(
            "descripcion", "Cookie de Chocolate",
            "precio", 29.99,
            "ingredientesIds", List.of(1)
        );

        // Act
        ResponseEntity<Menu> resultado = menuController.crearNuevoPlato(request);

        // Assert
        assertNotNull(resultado);
        assertEquals(HttpStatus.CREATED, resultado.getStatusCode());
        assertEquals("Cookie de Chocolate", resultado.getBody().getDescripcion());
        verify(menuService, times(1)).crearRegistro(any());
    }

    @Test
    @DisplayName("POST - Debe lanzar excepción si descripción es obligatoria")
    void testCrearPlatoSinDescripcion() {
        // Arrange
        Map<String, Object> request = Map.of(
            "precio", 29.99
        );

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> 
            menuController.crearNuevoPlato(request)
        );
        verify(menuService, never()).crearRegistro(any());
    }

    @Test
    @DisplayName("POST - Debe lanzar excepción si precio es obligatorio")
    void testCrearPlatoSinPrecio() {
        // Arrange
        Map<String, Object> request = Map.of(
            "descripcion", "Cookie de Chocolate"
        );

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> 
            menuController.crearNuevoPlato(request)
        );
        verify(menuService, never()).crearRegistro(any());
    }

    @Test
    @DisplayName("GET - Debe retornar lista de menús")
    void testObtenerTodos() {
        // Arrange
        List<Menu> menus = List.of(menu);
        when(menuService.obtenerTodosLosRegistros()).thenReturn(menus);

        // Act
        ResponseEntity<List<Menu>> resultado = menuController.obtenerTodos();

        // Assert
        assertNotNull(resultado);
        assertEquals(HttpStatus.OK, resultado.getStatusCode());
        assertEquals(1, resultado.getBody().size());
        verify(menuService, times(1)).obtenerTodosLosRegistros();
    }

    @Test
    @DisplayName("GET /{id} - Debe retornar menú por ID")
    void testObtenerPorId() {
        // Arrange
        when(menuService.obtenerRegistroPorId(1L)).thenReturn(menu);

        // Act
        ResponseEntity<Menu> resultado = menuController.obtenerPorId(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals(HttpStatus.OK, resultado.getStatusCode());
        assertEquals("Cookie de Chocolate", resultado.getBody().getDescripcion());
        verify(menuService, times(1)).obtenerRegistroPorId(1L);
    }

    @Test
    @DisplayName("GET /estado/disponibles - Debe retornar solo menús disponibles")
    void testObtenerDisponibles() {
        // Arrange
        List<Menu> menusDisponibles = List.of(menu);
        when(menuService.obtenerRegistrosDisponibles()).thenReturn(menusDisponibles);

        // Act
        ResponseEntity<List<Menu>> resultado = menuController.obtenerDisponibles();

        // Assert
        assertNotNull(resultado);
        assertEquals(HttpStatus.OK, resultado.getStatusCode());
        assertEquals(0, resultado.getBody().get(0).getEstado());
        verify(menuService, times(1)).obtenerRegistrosDisponibles();
    }

    @Test
    @DisplayName("GET /estado/no-disponibles - Debe retornar solo menús no disponibles")
    void testObtenerNoDisponibles() {
        // Arrange
        Menu menuNoDisponible = Menu.builder()
                .id(2L)
                .descripcion("Cookie Especial")
                .precio(BigDecimal.valueOf(39.99))
                .estado(1)
                .ingredientes(new ArrayList<>())
                .build();

        List<Menu> menusNoDisponibles = List.of(menuNoDisponible);
        when(menuService.obtenerRegistrosNoDisponibles()).thenReturn(menusNoDisponibles);

        // Act
        ResponseEntity<List<Menu>> resultado = menuController.obtenerNoDisponibles();

        // Assert
        assertNotNull(resultado);
        assertEquals(HttpStatus.OK, resultado.getStatusCode());
        assertEquals(1, resultado.getBody().get(0).getEstado());
        verify(menuService, times(1)).obtenerRegistrosNoDisponibles();
    }

    @Test
    @DisplayName("GET /{id}/ingredientes-faltantes - Debe retornar ingredientes faltantes")
    void testObtenerIngredientesFaltantes() {
        // Arrange
        List<String> ingredientesFaltantes = List.of("Vainilla", "Mantequilla");
        when(menuService.obtenerRegistroPorId(1L)).thenReturn(menu);
        when(validacionIngredienteService.obtenerIngredientesNoDisponibles(any()))
                .thenReturn(ingredientesFaltantes);

        // Act
        ResponseEntity<Map<String, Object>> resultado = menuController.obtenerIngredientesFaltantes(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals(HttpStatus.OK, resultado.getStatusCode());
        assertEquals(1L, resultado.getBody().get("id"));
        verify(menuService, times(1)).obtenerRegistroPorId(1L);
    }

    @Test
    @DisplayName("PUT /{id}/actualizar-estado - Debe actualizar el estado del menú")
    void testActualizarEstado() {
        // Arrange
        Menu menuActualizado = Menu.builder()
                .id(1L)
                .descripcion("Cookie de Chocolate")
                .precio(BigDecimal.valueOf(29.99))
                .estado(1)
                .ingredientes(new ArrayList<>())
                .build();

        when(menuService.actualizarEstadoRegistro(1L)).thenReturn(menuActualizado);

        // Act
        ResponseEntity<Menu> resultado = menuController.actualizarEstado(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals(HttpStatus.OK, resultado.getStatusCode());
        assertEquals(1, resultado.getBody().getEstado());
        verify(menuService, times(1)).actualizarEstadoRegistro(1L);
    }

    @Test
    @DisplayName("POST /{id}/agregar-ingredientes - Debe agregar ingredientes")
    void testAgregarIngredientes() {
        // Arrange
        Ingrediente vainilla = Ingrediente.builder()
                .id(2L)
                .nombre("Vainilla")
                .cantidad(100.0)
                .unidad("ml")
                .build();

        Menu menuConNuevosIngredientes = Menu.builder()
                .id(1L)
                .descripcion("Cookie de Chocolate")
                .precio(BigDecimal.valueOf(29.99))
                .estado(0)
                .ingredientes(new ArrayList<>(List.of(ingrediente, vainilla)))
                .build();

        when(menuService.agregarIngredientesAlRegistro(1L, List.of(2)))
                .thenReturn(menuConNuevosIngredientes);

        Map<String, Object> request = Map.of(
            "ingredientesIds", List.of(2)
        );

        // Act
        ResponseEntity<Menu> resultado = menuController.agregarIngredientes(1L, request);

        // Assert
        assertNotNull(resultado);
        assertEquals(HttpStatus.OK, resultado.getStatusCode());
        assertEquals(2, resultado.getBody().getIngredientes().size());
        verify(menuService, times(1)).agregarIngredientesAlRegistro(1L, List.of(2));
    }

    @Test
    @DisplayName("POST /{id}/agregar-ingredientes - Debe lanzar excepción si ingredientes vacío")
    void testAgregarIngredientesVacio() {
        // Arrange
        Map<String, Object> request = Map.of(
            "ingredientesIds", List.of()
        );

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> 
            menuController.agregarIngredientes(1L, request)
        );
        verify(menuService, never()).agregarIngredientesAlRegistro(any(), any());
    }
}
