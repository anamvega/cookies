package com.restaurant.cookie.gateway;

import com.restaurant.cookie.model.Menu;
import com.restaurant.cookie.repository.RegistroRepository;
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
public class RegistroGateway {

    private final RegistroRepository registroRepository;

    public Menu save(Menu menu) {
        return registroRepository.save(menu);
    }

    public List<Menu> findAll() {
        return registroRepository.findAll();
    }

    public Optional<Menu> findById(Long id) {
        return registroRepository.findById(id);
    }

    public void deleteById(Long id) {
        registroRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return registroRepository.existsById(id);
    }
}
