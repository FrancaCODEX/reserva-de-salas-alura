package br.com.francacodex.reservas.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ApiIntegrationTest {
    @Autowired MockMvc mvc;

    @Test void salaComCapacidadeInvalidaDevolve400ECampo() throws Exception {
        mvc.perform(post("/api/v1/salas").contentType(MediaType.APPLICATION_JSON)
            .content("{\"nome\":\"Azul\",\"capacidade\":0,\"ativa\":true}"))
            .andExpect(status().isBadRequest()).andExpect(jsonPath("$.campo").value("capacidade"));
    }
    @Test void salaNaoEncontradaDevolve404() throws Exception {
        mvc.perform(get("/api/v1/salas/999999")).andExpect(status().isNotFound())
            .andExpect(jsonPath("$.mensagem").exists());
    }
    @Test void usuarioComEmailInvalidoDevolve400() throws Exception {
        mvc.perform(post("/api/v1/usuarios").contentType(MediaType.APPLICATION_JSON)
            .content("{\"nome\":\"Ana\",\"email\":\"invalido\"}"))
            .andExpect(status().isBadRequest()).andExpect(jsonPath("$.campo").value("email"));
    }
}
