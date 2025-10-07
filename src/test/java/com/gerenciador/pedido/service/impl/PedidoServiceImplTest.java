package com.gerenciador.pedido.service.impl;

import com.gerenciador.auth.record.UsuarioLogadoRecord;
import com.gerenciador.auth.service.AuthService;
import com.gerenciador.exception.NotFoundException;
import com.gerenciador.exception.OrderNotBelongException;
import com.gerenciador.exception.OutOfStockOrderException;
import com.gerenciador.paginacao.PageRecord;
import com.gerenciador.pedido.entity.Pedido;
import com.gerenciador.pedido.entity.PedidoItens;
import com.gerenciador.pedido.enums.StatusPedido;
import com.gerenciador.pedido.mapper.PedidoMapper;
import com.gerenciador.pedido.record.ItemRecord;
import com.gerenciador.pedido.record.PagamentoRecord;
import com.gerenciador.pedido.record.PedidoRecord;
import com.gerenciador.pedido.repository.PedidoItemRepository;
import com.gerenciador.pedido.repository.PedidoRepository;
import com.gerenciador.produto.entity.Produto;
import com.gerenciador.produto.mapper.ProdutoMapper;
import com.gerenciador.produto.record.ProdutoRecord;
import com.gerenciador.produto.service.ProdutoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PedidoServiceImplTest {

    @Mock
    private ProdutoService produtoService;

    @Mock
    private PedidoRepository repository;

    @Mock
    private PedidoItemRepository itemRepository;

    @Mock
    private PedidoMapper mapper;

    @Mock
    private ProdutoMapper produtoMapper;

    @Mock
    private AuthService authService;

    @InjectMocks
    private PedidoServiceImpl service;

    private Pedido pedido;
    private PedidoRecord pedidoRecord;
    private ProdutoRecord produtoRecord;
    private Produto produto;
    private ItemRecord itemRecord;
    private PagamentoRecord pagamentoRecord;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        produtoRecord = new ProdutoRecord(1L, "Notebook", "Desc", BigDecimal.valueOf(5000), 1L, 5);
        produto = new Produto();
        produto.setId(1L);
        produto.setNome("Notebook");
        produto.setPreco(BigDecimal.valueOf(5000));
        produto.setQuantidadeEstoque(5);

        pedido = Pedido.builder()
                .id(1L)
                .usuario("user123")
                .valorTotal(BigDecimal.valueOf(10000))
                .status(StatusPedido.PENDENTE)
                .dataPedido(LocalDateTime.now())
                .build();

        pedidoRecord = new PedidoRecord(1L, "user123", StatusPedido.PENDENTE, BigDecimal.valueOf(10000), LocalDateTime.now(), LocalDateTime.now());
        itemRecord = new ItemRecord(1L, 1,2L);
        pagamentoRecord = new PagamentoRecord(BigDecimal.valueOf(10000));
        when(authService.getUsuarioLogado()).thenReturn( UsuarioLogadoRecord.builder().role("ROLE_ADMIN").username("user123").build());
    }

    @Test
    void deveSalvarPedidoComSucesso() {
        when(produtoService.buscarPorListaId(anyList())).thenReturn(List.of(produtoRecord));
        when(repository.save(any(Pedido.class))).thenReturn(pedido);
        when(mapper.toRecord(any(Pedido.class))).thenReturn(pedidoRecord);

        PedidoRecord resultado = service.salvar(List.of(itemRecord));

        assertNotNull(resultado);
        verify(repository, atLeastOnce()).save(any(Pedido.class));
        verify(itemRepository, atLeastOnce()).saveAll(anyList());
    }

    @Test
    void deveLancarExcecaoAoSalvarPedidoSemEstoque() {
        ProdutoRecord semEstoque = new ProdutoRecord(1L, "Notebook", "Desc", BigDecimal.valueOf(5000), 1L, 0);
        when(produtoService.buscarPorListaId(anyList())).thenReturn(List.of(semEstoque));

        assertThrows(OutOfStockOrderException.class, () -> service.salvar(List.of(itemRecord)));
    }

    @Test
    void deveEditarPedidoComSucesso() {
        PedidoItens itemExistente = PedidoItens.builder().idPedido(1L).idProduto(1L).quantidade(1).build();
        when(authService.getUsuarioLogado())
                .thenReturn(UsuarioLogadoRecord.builder()
                        .username("user123")
                        .role("ROLE_ADMIN")
                        .build());
        when(repository.findById(1L)).thenReturn(Optional.of(pedido));
        when(produtoService.buscarPorListaId(anyList())).thenReturn(List.of(produtoRecord));
        when(itemRepository.findByIdPedido(1L)).thenReturn(List.of(itemExistente));
        when(mapper.toRecord(any(Pedido.class))).thenReturn(pedidoRecord);
        when(repository.save(pedido)).thenReturn(pedido);

        PedidoRecord resultado = service.editar(1L, List.of(itemRecord));

        assertNotNull(resultado);
        verify(itemRepository, atLeastOnce()).saveAll(anyList());
    }

    @Test
    void deveLancarExcecaoAoEditarPedidoDeOutroUsuario() {
        Pedido outroPedido = Pedido.builder().id(1L).usuario("outro").build();
        when(repository.findById(1L)).thenReturn(Optional.of(outroPedido));
        when(authService.getUsuarioLogado()).thenReturn(UsuarioLogadoRecord.builder().role("ROLE_ADMIN").username("user123").build());

        assertThrows(OrderNotBelongException.class, () -> service.editar(1L, List.of(itemRecord)));
    }

    @Test
    void deveListarPedidosComSucesso() {
        Page page = mock(Page.class);
        when(authService.getUsuarioLogado()).thenReturn(UsuarioLogadoRecord.builder().role("ROLE_ADMIN").username("user123").build());
        when(repository.buscarPedidosComProdutos(any(), any(), any(Pageable.class))).thenReturn(page);
        when(mapper.toPageRecord(page)).thenReturn(new PageRecord(List.of(pedidoRecord), 1, 1, 1, 1, Boolean.TRUE, Boolean.TRUE, 1, Boolean.FALSE));

        PageRecord resultado = service.listar("user123", "PENDENTE", Pageable.unpaged());

        assertNotNull(resultado);
        verify(repository).buscarPedidosComProdutos(any(), any(), any(Pageable.class));
    }

    @Test
    void deveRealizarPagamentoComSucesso() {
        pedido.setStatus(StatusPedido.PENDENTE);
        when(repository.findById(1L)).thenReturn(Optional.of(pedido));
        when(itemRepository.findByIdPedido(1L)).thenReturn(List.of(PedidoItens.builder().idProduto(1L).quantidade(1).build()));
        when(produtoService.buscarPorId(1L)).thenReturn(produtoRecord);
        when(produtoMapper.toEntity(produtoRecord)).thenReturn(produto);
        when(repository.save(any(Pedido.class))).thenReturn(pedido);
        when(mapper.toRecord(pedido)).thenReturn(pedidoRecord);

        PedidoRecord resultado = service.pagamento(1L, pagamentoRecord);

        assertNotNull(resultado);
        verify(repository).save(any(Pedido.class));
    }

    @Test
    void deveLancarExcecaoAoPagamentoComValorDiferente() {
        Pedido pedido = Pedido.builder().id(1L).valorTotal(BigDecimal.valueOf(200)).usuario("user123").status(StatusPedido.PENDENTE).build();
        when(repository.findById(1L)).thenReturn(Optional.of(pedido));

        assertThrows(IllegalArgumentException.class, () -> service.pagamento(1L, pagamentoRecord));
    }

    @Test
    void deveLancarExcecaoAoPagamentoPedidoJaPago() {
        Pedido pedidoPago = Pedido.builder().id(1L).valorTotal(BigDecimal.valueOf(10000)).usuario("user123").status(StatusPedido.CONCLUIDO).build();
        when(repository.findById(1L)).thenReturn(Optional.of(pedidoPago));

        assertThrows(IllegalArgumentException.class, () -> service.pagamento(1L, pagamentoRecord));
    }

    @Test
    void deveLancarExcecaoAoPedidoNaoEncontrado() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.pagamento(1L, pagamentoRecord));
    }
}
