package com.gerenciador.produto.service.impl;

import com.gerenciador.exception.NotFoundException;
import com.gerenciador.paginacao.PageRecord;
import com.gerenciador.produto.entity.Produto;
import com.gerenciador.produto.mapper.ProdutoMapper;
import com.gerenciador.produto.record.CategoriaRecord;
import com.gerenciador.produto.record.ProdutoRecord;
import com.gerenciador.produto.repository.ProdutoRepository;
import com.gerenciador.produto.service.CategoriaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProdutoServiceImplTest {

    @Mock
    private ProdutoRepository repository;

    @Mock
    private ProdutoMapper mapper;

    @Mock
    private CategoriaService categoriaService;

    @InjectMocks
    private ProdutoServiceImpl service;

    private Produto produto;
    private ProdutoRecord produtoRecord;
    private CategoriaRecord categoriaRecord;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        categoriaRecord = new CategoriaRecord(1L, "Eletrônicos", "Itens de tecnologia");

        produto = new Produto();
        produto.setId(1L);
        produto.setNome("Notebook");
        produto.setDescricao("Notebook gamer");
        produto.setPreco(BigDecimal.valueOf(5000));
        produto.setIdCategoria(1L);
        produto.setQuantidadeEstoque(10);

        produtoRecord = new ProdutoRecord(
                1L,
                "Notebook",
                "Notebook gamer",
                BigDecimal.valueOf(5000),
                1L,
                10
        );
    }

    @Test
    void deveSalvarProdutoComSucesso() {
        when(categoriaService.buscarPorId(1L)).thenReturn(categoriaRecord);
        when(mapper.toSaveEntity(produtoRecord)).thenReturn(produto);
        when(repository.save(produto)).thenReturn(produto);
        when(mapper.toRecord(produto)).thenReturn(produtoRecord);

        ProdutoRecord resultado = service.salvar(produtoRecord);

        assertNotNull(resultado);
        assertEquals("Notebook", resultado.nome());
        verify(repository).save(produto);
        verify(categoriaService).buscarPorId(1L);
    }

    @Test
    void deveLancarExcecaoAoSalvarComCategoriaInexistente() {
        when(categoriaService.buscarPorId(1L)).thenReturn(null);

        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> service.salvar(produtoRecord)
        );

        assertEquals("Nenhuma categoria encontrada com ID = 1", exception.getMessage());
        verify(repository, never()).save(any());
    }

    @Test
    void deveListarProdutosComSucesso() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Produto> produtoPage = new PageImpl<>(List.of(produto));
        PageRecord pageRecord = new PageRecord(List.of(produtoRecord), 1, 1, 1, 1, Boolean.TRUE, Boolean.TRUE, 1, Boolean.FALSE);

        when(repository.buscarPorFiltros(
                any(), any(), any(), any(), any(), eq(pageable))
        ).thenReturn(produtoPage);
        when(mapper.toPageRecord(produtoPage)).thenReturn(pageRecord);

        PageRecord resultado = service.listar(
                "Notebook", "gamer", BigDecimal.valueOf(5000),
                1L, 10, pageable
        );

        assertNotNull(resultado);
        assertEquals(1, resultado.totalElements());
        verify(repository).buscarPorFiltros(any(), any(), any(), any(), any(), eq(pageable));
    }

    @Test
    void deveBuscarProdutoPorIdComSucesso() {
        when(repository.findById(1L)).thenReturn(Optional.of(produto));
        when(mapper.toRecord(produto)).thenReturn(produtoRecord);

        ProdutoRecord resultado = service.buscarPorId(1L);

        assertNotNull(resultado);
        assertEquals("Notebook", resultado.nome());
    }

    @Test
    void deveRetornarNullAoBuscarProdutoInexistente() {
        when(repository.findById(99L)).thenReturn(Optional.empty());
        when(mapper.toRecord(null)).thenReturn(null);

        ProdutoRecord resultado = service.buscarPorId(99L);

        assertNull(resultado);
    }

    @Test
    void deveBuscarListaDeProdutosPorIds() {
        List<Long> ids = List.of(1L, 2L);
        when(repository.findAllById(ids)).thenReturn(List.of(produto));
        when(mapper.toRecordList(List.of(produto))).thenReturn(List.of(produtoRecord));

        List<ProdutoRecord> resultado = service.buscarPorListaId(ids);

        assertEquals(1, resultado.size());
        verify(repository).findAllById(ids);
    }

    @Test
    void deveAtualizarProdutoComSucesso() {
        Produto produtoAtualizado = new Produto();
        produtoAtualizado.setId(1L);
        produtoAtualizado.setNome("Notebook Pro");
        produtoAtualizado.setDescricao("Atualizado");
        produtoAtualizado.setPreco(BigDecimal.valueOf(6000));
        produtoAtualizado.setIdCategoria(1L);
        produtoAtualizado.setQuantidadeEstoque(5);

        ProdutoRecord novoRecord = new ProdutoRecord(1L, "Notebook Pro", "Atualizado", BigDecimal.valueOf(6000), 1L, 5);

        when(repository.findById(1L)).thenReturn(Optional.of(produto));
        when(repository.save(any(Produto.class))).thenReturn(produtoAtualizado);
        when(mapper.toRecord(produtoAtualizado)).thenReturn(novoRecord);

        ProdutoRecord resultado = service.atualizar(1L, novoRecord);

        assertEquals("Notebook Pro", resultado.nome());
        assertEquals(BigDecimal.valueOf(6000), resultado.preco());
        verify(repository).save(any(Produto.class));
    }

    @Test
    void deveLancarExcecaoAoAtualizarProdutoInexistente() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> service.atualizar(99L, produtoRecord)
        );

        assertEquals("Produto não encontrado", exception.getMessage());
        verify(repository, never()).save(any());
    }

    @Test
    void deveDeletarProdutoComSucesso() {
        when(repository.findById(1L)).thenReturn(Optional.of(produto));

        service.deletar(1L);

        verify(repository).delete(produto);
    }

    @Test
    void deveLancarExcecaoAoDeletarProdutoInexistente() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> service.deletar(99L)
        );

        assertEquals("Produto não encontrado", exception.getMessage());
        verify(repository, never()).delete(any());
    }
}
