package com.example.cookie.repository;

import com.example.cookie.model.Registro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegistroRepository extends JpaRepository<Registro, Long> {
    List<Registro> findAll();
}
