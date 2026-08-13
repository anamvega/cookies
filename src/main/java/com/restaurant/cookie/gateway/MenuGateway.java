package com.restaurant.cookie.gateway;

import com.restaurant.cookie.model.Menu;
import com.restaurant.cookie.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Patrón Gateway: actúa como intermediario entre la capa de servicio
 * y la capa de repositorio, abstrayendo el acceso directo a datos.
 */
@Component
@RequiredArgsConstructor
public class MenuGateway {

    private final MenuRepository menuRepository;

    public Menu save(Menu menu) {
        return menuRepository.save(menu);
    }

    public List<Menu> findAll() {
        return menuRepository.findAll();
    }

    public Optional<Menu> findById(Long id) {
        return menuRepository.findById(id);
    }

    public void deleteById(Long id) {
        menuRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return menuRepository.existsById(id);
    }
}
