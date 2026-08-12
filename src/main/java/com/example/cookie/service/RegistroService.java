package com.example.cookie.service;

import com.example.cookie.gateway.RegistroGateway;
import com.example.cookie.model.Registro;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RegistroService {

    private final RegistroGateway registroGateway;

    public Registro crearRegistro(Registro registro) {
        return registroGateway.save(registro);
    }

    public List<Registro> obtenerTodosLosRegistros() {
        return registroGateway.findAll();
    }

    public Registro obtenerRegistroPorId(Long id) {
        return registroGateway.findById(id)
                .orElseThrow(() -> new RuntimeException("Registro no encontrado con id: " + id));
    }
}
