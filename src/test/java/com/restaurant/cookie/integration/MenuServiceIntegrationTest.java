package com.restaurant.cookie.integration;

import com.restaurant.cookie.model.Ingredient;
import com.restaurant.cookie.model.Menu;
import com.restaurant.cookie.repository.IngredientRepository;
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
    private IngredientRepository ingredientRepository;

    private Ingredient ingredient;

    @BeforeEach
    void setUp() {
        menuRepository.deleteAll();
        ingredientRepository.deleteAll();

        ingredient = Ingredient.builder()
                .name("Chocolate")
                .quantity(500.0)
                .unit("g")
                .build();
        ingredientRepository.save(ingredient);
    }

    @Test
    @DisplayName("Crear y obtener menú")
    void testCrearYObtenerMenu() {
        // Arrange
        Menu newMenu = Menu.builder()
                .description("Chocolate Cookie")
                .price(BigDecimal.valueOf(29.99))
                .ingredients(new ArrayList<>(List.of(ingredient)))
                .build();

        // Act
        Menu menuGuardado = menuService.crearRegistro(newMenu);

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
                .description("Cookie 1")
                .price(BigDecimal.valueOf(25.0))
                .ingredients(new ArrayList<>())
                .build();
        Menu menu2 = Menu.builder()
                .description("Cookie 2")
                .price(BigDecimal.valueOf(30.0))
                .ingredients(new ArrayList<>())
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
        assertTrue(menusAvailable.stream().allMatch(m -> m.getStatus() == 0));
    }

    @Test
    @DisplayName("Agregar ingredients a menú")
    void testAgregarIngredientesAlMenu() {
        // Arrange
        Menu menu = Menu.builder()
                .description("New Cookie")
                .price(BigDecimal.valueOf(25.0))
                .ingredients(new ArrayList<>())
                .build();
        Menu savedMenu = menuService.createRecord(menu);

        // Act
        Menu menuConIngredientes = menuService.agregarIngredientesAlRegistro(
            menuGuardado.getId(), 
            List.of(ingredient.getId().intValue())
        );

        // Assert
        assertNotNull(menuConIngredientes);
        assertTrue(menuConIngredientes.getIngredients().size() > 0);
    }
}
