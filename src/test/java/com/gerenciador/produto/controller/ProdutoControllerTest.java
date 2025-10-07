package com.gerenciador.produto.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gerenciador.paginacao.PageRecord;
import com.gerenciador.produto.record.ProdutoRecord;
import com.gerenciador.produto.service.ProdutoService;
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
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SuppressWarnings("removal")
@WebMvcTest(
        controllers = ProdutoController.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = JwtFilter.class
        )
)
@AutoConfigureMockMvc(addFilters = false)
class ProdutoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProdutoService service;

    @Autowired
    private ObjectMapper objectMapper;

    private ProdutoRecord produtoRecord;

    @BeforeEach
    void setUp() {
        produtoRecord = new ProdutoRecord(
                1L,
                "Notebook",
                "Notebook Gamer",
                BigDecimal.valueOf(5000),
                1L,
                10
        );
    }

    @Test
    void deveCriarProdutoComSucesso() throws Exception {
        when(service.salvar(any(ProdutoRecord.class))).thenReturn(produtoRecord);

        mockMvc.perform(post("/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(produtoRecord)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("Notebook"))
                .andExpect(jsonPath("$.descricao").value("Notebook Gamer"));

        verify(service).salvar(any(ProdutoRecord.class));
    }

    @Test
    void deveAtualizarProdutoComSucesso() throws Exception {
        ProdutoRecord atualizado = new ProdutoRecord(
                1L,
                "Notebook Pro",
                "Atualizado",
                BigDecimal.valueOf(6000),
                1L,
                8
        );

        when(service.atualizar(eq(1L), any(ProdutoRecord.class))).thenReturn(atualizado);

        mockMvc.perform(put("/produtos/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(atualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Notebook Pro"))
                .andExpect(jsonPath("$.descricao").value("Atualizado"))
                .andExpect(jsonPath("$.preco").value(6000));

        verify(service).atualizar(eq(1L), any(ProdutoRecord.class));
    }

    @Test
    void deveListarProdutosComSucesso() throws Exception {
        PageRecord pageRecord = new PageRecord(
                java.util.List.of(produtoRecord),
                1,
                1,
                1,
                1,
                Boolean.TRUE,
                Boolean.TRUE,
                1,
                Boolean.FALSE
        );

        when(service.listar(anyString(), anyString(), any(), any(), any(), any(PageRequest.class)))
                .thenReturn(pageRecord);

        mockMvc.perform(get("/produtos")
                        .param("nome", "Notebook")
                        .param("descricao", "Gamer")
                        .param("preco", "5000")
                        .param("idCategoria", "1")
                        .param("quantidadeEstoque", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[0].nome").value("Notebook"))
                .andExpect(jsonPath("$.content[0].descricao").value("Notebook Gamer"));

        verify(service).listar(anyString(), anyString(), any(), any(), any(), any(PageRequest.class));
    }

    @Test
    void deveBuscarProdutoPorIdComSucesso() throws Exception {
        when(service.buscarPorId(1L)).thenReturn(produtoRecord);

        mockMvc.perform(get("/produtos/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("Notebook"));

        verify(service).buscarPorId(1L);
    }

    @Test
    void deveDeletarProdutoComSucesso() throws Exception {
        doNothing().when(service).deletar(1L);

        mockMvc.perform(delete("/produtos/{id}", 1L))
                .andExpect(status().isNoContent());

        verify(service).deletar(1L);
    }
}
