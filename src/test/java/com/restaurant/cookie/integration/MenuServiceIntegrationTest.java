package com.restaurant.cookie.integration;

import com.restaurant.cookie.model.Ingrediente;
import com.restaurant.cookie.model.Menu;
import com.restaurant.cookie.repository.IngredienteRepository;
import com.restaurant.cookie.repository.MenuRepository;
import com.restaurant.cookie.service.MenuService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DisplayName("MenuService Integration Tests")
class MenuServiceIntegrationTest {

    @Autowired
    private MenuService menuService;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private IngredienteRepository ingredienteRepository;

    private Ingrediente ingrediente;

    @BeforeEach
    void setUp() {
        menuRepository.deleteAll();
        ingredienteRepository.deleteAll();

        ingrediente = Ingrediente.builder()
                .nombre("Chocolate")
                .cantidad(500.0)
                .unidad("g")
                .build();
        ingredienteRepository.save(ingrediente);
    }

    @Test
    @DisplayName("Crear y obtener menú")
    void testCrearYObtenerMenu() {
        // Arrange
        Menu nuevoMenu = Menu.builder()
                .descripcion("Cookie de Chocolate")
                .precio(BigDecimal.valueOf(29.99))
                .ingredientes(new ArrayList<>(List.of(ingrediente)))
                .build();

        // Act
        Menu menuGuardado = menuService.crearRegistro(nuevoMenu);

        // Assert
        assertNotNull(menuGuardado);
        assertNotNull(menuGuardado.getId());
        assertEquals("Cookie de Chocolate", menuGuardado.getDescripcion());
    }

    @Test
    @DisplayName("Obtener todos los menús")
    void testObtenerTodosLosMenus() {
        // Arrange
        Menu menu1 = Menu.builder()
                .descripcion("Cookie 1")
                .precio(BigDecimal.valueOf(25.0))
                .ingredientes(new ArrayList<>())
                .build();
        Menu menu2 = Menu.builder()
                .descripcion("Cookie 2")
                .precio(BigDecimal.valueOf(30.0))
                .ingredientes(new ArrayList<>())
                .build();

        menuService.crearRegistro(menu1);
        menuService.crearRegistro(menu2);

        // Act
        List<Menu> menus = menuService.obtenerTodosLosRegistros();

        // Assert
        assertNotNull(menus);
        assertTrue(menus.size() >= 2);
    }

    @Test
    @DisplayName("Filtrar menús disponibles")
    void testFiltrarMenusDisponibles() {
        // Act
        List<Menu> menusDisponibles = menuService.obtenerRegistrosDisponibles();

        // Assert
        assertNotNull(menusDisponibles);
        assertTrue(menusDisponibles.stream().allMatch(m -> m.getEstado() == 0));
    }

    @Test
    @DisplayName("Agregar ingredientes a menú")
    void testAgregarIngredientesAlMenu() {
        // Arrange
        Menu menu = Menu.builder()
                .descripcion("Cookie Nueva")
                .precio(BigDecimal.valueOf(25.0))
                .ingredientes(new ArrayList<>())
                .build();
        Menu menuGuardado = menuService.crearRegistro(menu);

        // Act
        Menu menuConIngredientes = menuService.agregarIngredientesAlRegistro(
            menuGuardado.getId(), 
            List.of(ingrediente.getId().intValue())
        );

        // Assert
        assertNotNull(menuConIngredientes);
        assertTrue(menuConIngredientes.getIngredientes().size() > 0);
    }
}
