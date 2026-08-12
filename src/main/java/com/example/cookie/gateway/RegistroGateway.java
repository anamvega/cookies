package com.example.cookie.gateway;

import com.example.cookie.model.Registro;
import com.example.cookie.repository.RegistroRepository;
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

    public Registro save(Registro registro) {
        return registroRepository.save(registro);
    }

    public List<Registro> findAll() {
        return registroRepository.findAll();
    }

    public Optional<Registro> findById(Long id) {
        return registroRepository.findById(id);
    }

    public void deleteById(Long id) {
        registroRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return registroRepository.existsById(id);
    }
}
