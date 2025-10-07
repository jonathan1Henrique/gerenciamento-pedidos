package com.gerenciador.pedido.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gerenciador.paginacao.PageRecord;
import com.gerenciador.pedido.enums.StatusPedido;
import com.gerenciador.pedido.record.ItemRecord;
import com.gerenciador.pedido.record.PagamentoRecord;
import com.gerenciador.pedido.record.PedidoRecord;
import com.gerenciador.pedido.service.PedidoService;
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
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SuppressWarnings("removal")
@WebMvcTest(
        controllers = PedidoController.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = JwtFilter.class
        )
)
@AutoConfigureMockMvc(addFilters = false)
class PedidoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PedidoService service;

    @Autowired
    private ObjectMapper objectMapper;

    private PedidoRecord pedidoRecord;
    private ItemRecord itemRecord;
    private PagamentoRecord pagamentoRecord;

    @BeforeEach
    void setUp() {
        itemRecord = new ItemRecord(1L, 1,2L);
        pagamentoRecord = new PagamentoRecord(BigDecimal.valueOf(10.00));
        pedidoRecord = new PedidoRecord(1L, "user123", StatusPedido.CONCLUIDO,BigDecimal.valueOf(10.00), LocalDateTime.now(), LocalDateTime.now());
    }

    @Test
    void deveCriarPedidoComSucesso() throws Exception {
        when(service.salvar(anyList())).thenReturn(pedidoRecord);

        mockMvc.perform(post("/pedidos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(List.of(itemRecord))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.usuario").value("user123"))
                .andExpect(jsonPath("$.statusPedido").value(StatusPedido.CONCLUIDO.toString()));

        verify(service).salvar(anyList());
    }

    @Test
    void deveEditarPedidoComSucesso() throws Exception {
        when(service.editar(eq(1L), anyList())).thenReturn(pedidoRecord);

        mockMvc.perform(put("/pedidos/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(List.of(itemRecord))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.usuario").value("user123"));

        verify(service).editar(eq(1L), anyList());
    }

    @Test
    void deveRealizarPagamentoComSucesso() throws Exception {
        when(service.pagamento(eq(1L), any(PagamentoRecord.class))).thenReturn(pedidoRecord);

        mockMvc.perform(post("/pedidos/pagamento/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pagamentoRecord)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.statusPedido").value("CONCLUIDO"));

        verify(service).pagamento(eq(1L), any(PagamentoRecord.class));
    }

    @Test
    void deveListarPedidosComSucesso() throws Exception {
        PageRecord pageRecord = new PageRecord(
                java.util.List.of(pedidoRecord),
                1,
                1,
                1,
                1,
                Boolean.TRUE,
                Boolean.TRUE,
                1,
                Boolean.FALSE
        );

        when(service.listar(anyString(), anyString(), any(PageRequest.class)))
                .thenReturn(pageRecord);

        mockMvc.perform(get("/pedidos")
                        .param("usuario", "user123")
                        .param("status", "PENDENTE")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[0].usuario").value("user123"))
                .andExpect(jsonPath("$.content[0].statusPedido").value("CONCLUIDO"));

        verify(service).listar(anyString(), anyString(), any(PageRequest.class));
    }
}
