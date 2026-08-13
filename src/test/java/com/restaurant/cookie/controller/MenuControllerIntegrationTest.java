package com.restaurant.cookie.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MenuControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void shouldCreateMenuSuccessfully() {
        Map<String, Object> payload = Map.of(
                "descripcion", "Pizza Margarita",
                "precio", 120.5,
                "ingredients", List.of("Queso", "Tomate")
        );

        ResponseEntity<Map> response = restTemplate.postForEntity("/registros", payload, Map.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().get("descripcion")).isEqualTo("Pizza Margarita");
    }
}
