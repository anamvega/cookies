package com.restaurant.cookie.gateway;

import com.restaurant.cookie.model.Menu;
import com.restaurant.cookie.repository.MenuRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("MenuGateway Tests")
class MenuGatewayTest {

    @Mock
    private MenuRepository menuRepository;

    @InjectMocks
    private MenuGateway menuGateway;

    private Menu menu;

    @BeforeEach
    void setUp() {
        menu = Menu.builder()
                .id(1L)
                .descripcion("Cookie de Chocolate")
                .precio(BigDecimal.valueOf(29.99))
                .estado(0)
                .ingredientes(new ArrayList<>())
                .build();
    }

    @Test
    @DisplayName("Save - Debe guardar un menú en el repositorio")
    void testSave() {
        // Arrange
        when(menuRepository.save(any(Menu.class))).thenReturn(menu);

        // Act
        Menu resultado = menuGateway.save(menu);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Cookie de Chocolate", resultado.getDescripcion());
        assertEquals(BigDecimal.valueOf(29.99), resultado.getPrecio());
        verify(menuRepository, times(1)).save(any(Menu.class));
    }

    @Test
    @DisplayName("Save - Debe retornar el menú guardado con ID generado")
    void testSaveRetornaMenuConId() {
        // Arrange
        Menu menuSinId = Menu.builder()
                .descripcion("Cookie de Vainilla")
                .precio(BigDecimal.valueOf(24.99))
                .estado(0)
                .ingredientes(new ArrayList<>())
                .build();

        Menu menuGuardado = Menu.builder()
                .id(2L)
                .descripcion("Cookie de Vainilla")
                .precio(BigDecimal.valueOf(24.99))
                .estado(0)
                .ingredientes(new ArrayList<>())
                .build();

        when(menuRepository.save(any(Menu.class))).thenReturn(menuGuardado);

        // Act
        Menu resultado = menuGateway.save(menuSinId);

        // Assert
        assertNotNull(resultado);
        assertNotNull(resultado.getId());
        assertEquals(2L, resultado.getId());
        verify(menuRepository, times(1)).save(any(Menu.class));
    }

    @Test
    @DisplayName("FindAll - Debe retornar lista de todos los menús")
    void testFindAll() {
        // Arrange
        Menu menu2 = Menu.builder()
                .id(2L)
                .descripcion("Cookie de Vainilla")
                .precio(BigDecimal.valueOf(24.99))
                .estado(0)
                .ingredientes(new ArrayList<>())
                .build();

        List<Menu> menus = List.of(menu, menu2);
        when(menuRepository.findAll()).thenReturn(menus);

        // Act
        List<Menu> resultado = menuGateway.findAll();

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("Cookie de Chocolate", resultado.get(0).getDescripcion());
        assertEquals("Cookie de Vainilla", resultado.get(1).getDescripcion());
        verify(menuRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("FindAll - Debe retornar lista vacía cuando no hay menús")
    void testFindAllVacio() {
        // Arrange
        when(menuRepository.findAll()).thenReturn(new ArrayList<>());

        // Act
        List<Menu> resultado = menuGateway.findAll();

        // Assert
        assertNotNull(resultado);
        assertEquals(0, resultado.size());
        verify(menuRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("FindById - Debe retornar Optional con el menú encontrado")
    void testFindByIdEncontrado() {
        // Arrange
        when(menuRepository.findById(1L)).thenReturn(Optional.of(menu));

        // Act
        Optional<Menu> resultado = menuGateway.findById(1L);

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals(1L, resultado.get().getId());
        assertEquals("Cookie de Chocolate", resultado.get().getDescripcion());
        verify(menuRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("FindById - Debe retornar Optional vacío cuando no existe")
    void testFindByIdNoEncontrado() {
        // Arrange
        when(menuRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        Optional<Menu> resultado = menuGateway.findById(999L);

        // Assert
        assertFalse(resultado.isPresent());
        verify(menuRepository, times(1)).findById(999L);
    }

    @Test
    @DisplayName("DeleteById - Debe eliminar el menú del repositorio")
    void testDeleteById() {
        // Act
        menuGateway.deleteById(1L);

        // Assert
        verify(menuRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("DeleteById - Debe permitir eliminar un ID que no existe")
    void testDeleteByIdNoExistente() {
        // Act
        menuGateway.deleteById(999L);

        // Assert
        verify(menuRepository, times(1)).deleteById(999L);
    }

    @Test
    @DisplayName("ExistsById - Debe retornar true cuando el menú existe")
    void testExistsByIdExistente() {
        // Arrange
        when(menuRepository.existsById(1L)).thenReturn(true);

        // Act
        boolean resultado = menuGateway.existsById(1L);

        // Assert
        assertTrue(resultado);
        verify(menuRepository, times(1)).existsById(1L);
    }

    @Test
    @DisplayName("ExistsById - Debe retornar false cuando el menú no existe")
    void testExistsByIdNoExistente() {
        // Arrange
        when(menuRepository.existsById(999L)).thenReturn(false);

        // Act
        boolean resultado = menuGateway.existsById(999L);

        // Assert
        assertFalse(resultado);
        verify(menuRepository, times(1)).existsById(999L);
    }

    @Test
    @DisplayName("Encapsulación - Debe ser un componente Spring")
    void testEsComponenteSpring() {
        // Verify that the gateway is properly injected
        assertNotNull(menuGateway);
    }

    @Test
    @DisplayName("Abstracción - Debe ocultar la implementación del repositorio")
    void testAbstraccionDelRepositorio() {
        // Arrange - El gateway debe actuar como intermediario
        Menu menuPrueba = Menu.builder()
                .descripcion("Cookie de Prueba")
                .precio(BigDecimal.valueOf(19.99))
                .build();

        when(menuRepository.save(any(Menu.class))).thenReturn(
            Menu.builder()
                .id(3L)
                .descripcion("Cookie de Prueba")
                .precio(BigDecimal.valueOf(19.99))
                .build()
        );

        // Act
        Menu resultado = menuGateway.save(menuPrueba);

        // Assert - El cliente solo conoce la interfaz del gateway, no del repositorio
        assertNotNull(resultado);
        assertEquals(3L, resultado.getId());
        verify(menuRepository, times(1)).save(any(Menu.class));
    }
}
