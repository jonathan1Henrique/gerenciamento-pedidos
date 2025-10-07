package com.gerenciador.produto.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.gerenciador.produto.record.CategoriaRecord;
import com.gerenciador.produto.service.CategoriaService;
import com.gerenciador.security.JwtFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SuppressWarnings("removal")
@WebMvcTest(
        controllers = CategoriaController.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = JwtFilter.class
        )
)
@AutoConfigureMockMvc(addFilters = false)
class CategoriaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoriaService service;

    @Autowired
    private ObjectMapper objectMapper;

    private CategoriaRecord categoriaRecord;

    @BeforeEach
    void setUp() {
        categoriaRecord = new CategoriaRecord(1L, "Eletrônicos", "Itens de tecnologia");
    }

    @Test
    void deveSalvarCategoriaComSucesso() throws Exception {
        when(service.salvar(any(CategoriaRecord.class))).thenReturn(categoriaRecord);

        mockMvc.perform(post("/categorias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoriaRecord)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("Eletrônicos"))
                .andExpect(jsonPath("$.descricao").value("Itens de tecnologia"));

        verify(service).salvar(any(CategoriaRecord.class));
    }

    @Test
    void deveEditarCategoriaComSucesso() throws Exception {
        CategoriaRecord categoriaEditada = new CategoriaRecord(1L, "Eletrodomésticos", "Atualizado");

        when(service.atualizar(eq(1L), any(CategoriaRecord.class))).thenReturn(categoriaEditada);

        mockMvc.perform(put("/categorias/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoriaEditada)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Eletrodomésticos"))
                .andExpect(jsonPath("$.descricao").value("Atualizado"));

        verify(service).atualizar(eq(1L), any(CategoriaRecord.class));
    }

    @Test
    void deveListarCategoriasComSucesso() throws Exception {
        when(service.listar("Eletrônicos", "tecnologia"))
                .thenReturn(List.of(categoriaRecord));

        mockMvc.perform(get("/categorias")
                        .param("nome", "Eletrônicos")
                        .param("descricao", "tecnologia")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].nome").value("Eletrônicos"))
                .andExpect(jsonPath("$[0].descricao").value("Itens de tecnologia"));

        verify(service).listar("Eletrônicos", "tecnologia");
    }

    @Test
    void deveDeletarCategoriaComSucesso() throws Exception {
        doNothing().when(service).deletar(1L);

        mockMvc.perform(delete("/categorias")
                        .param("id", "1"))
                .andExpect(status().isNoContent());

        verify(service).deletar(1L);
    }
}
