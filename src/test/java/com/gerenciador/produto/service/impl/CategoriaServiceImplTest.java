package com.gerenciador.produto.service.impl;


import com.gerenciador.exception.NotFoundException;
import com.gerenciador.produto.entity.Categoria;
import com.gerenciador.produto.mapper.CategoriaMapper;
import com.gerenciador.produto.record.CategoriaRecord;
import com.gerenciador.produto.repository.CategoriaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class CategoriaServiceImplTest {

    @Mock
    private CategoriaRepository repository;

    @Mock
    private CategoriaMapper mapper;

    @InjectMocks
    private CategoriaServiceImpl service;

    private Categoria categoria;
    private CategoriaRecord categoriaRecord;

    @BeforeEach
    void setUp() {
        categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNome("Eletrônicos");
        categoria.setDescricao("Itens eletrônicos em geral");

        categoriaRecord = new CategoriaRecord(1L, "Eletrônicos", "Itens eletrônicos em geral");
    }

    @Test
    void deveSalvarCategoriaComSucesso() {
        when(mapper.toSaveEntity(categoriaRecord)).thenReturn(categoria);
        when(repository.save(categoria)).thenReturn(categoria);
        when(mapper.toRecord(categoria)).thenReturn(categoriaRecord);

        CategoriaRecord resultado = service.salvar(categoriaRecord);

        assertNotNull(resultado);
        assertEquals(categoriaRecord.nome(), resultado.nome());
        verify(repository, times(1)).save(categoria);
    }

    @Test
    void deveListarCategoriasComSucesso() {
        when(repository.buscarPorNomeOuDescricao("Eletrônicos", "Itens"))
                .thenReturn(List.of(categoria));
        when(mapper.toRecordList(List.of(categoria)))
                .thenReturn(List.of(categoriaRecord));

        List<CategoriaRecord> resultado = service.listar("Eletrônicos", "Itens");

        assertEquals(1, resultado.size());
        verify(repository, times(1)).buscarPorNomeOuDescricao("Eletrônicos", "Itens");
    }

    @Test
    void deveLancarExcecaoAoNaoEncontrarCategorias() {
        when(repository.buscarPorNomeOuDescricao(anyString(), anyString()))
                .thenReturn(Collections.emptyList());

        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> service.listar("Inexistente", "Nada")
        );

        assertEquals("Nenhuma categoria encontrada com os critérios informados.", exception.getMessage());
    }

    @Test
    void deveBuscarCategoriaPorIdComSucesso() {
        when(repository.findById(1L)).thenReturn(Optional.of(categoria));
        when(mapper.toRecord(categoria)).thenReturn(categoriaRecord);

        CategoriaRecord resultado = service.buscarPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.id());
    }

    @Test
    void deveRetornarNullAoBuscarCategoriaInexistente() {
        when(repository.findById(99L)).thenReturn(Optional.empty());
        when(mapper.toRecord(null)).thenReturn(null);

        CategoriaRecord resultado = service.buscarPorId(99L);

        assertNull(resultado);
    }

    @Test
    void deveAtualizarCategoriaComSucesso() {
        Categoria categoriaAtualizada = new Categoria();
        categoriaAtualizada.setId(1L);
        categoriaAtualizada.setNome("Eletrodomésticos");
        categoriaAtualizada.setDescricao("Atualizado");

        CategoriaRecord novaCategoriaRecord = new CategoriaRecord(null, "Eletrodomésticos", "Atualizado");

        when(repository.findById(1L)).thenReturn(Optional.of(categoria));
        when(repository.save(any(Categoria.class))).thenReturn(categoriaAtualizada);
        when(mapper.toRecord(categoriaAtualizada)).thenReturn(
                new CategoriaRecord(1L, "Eletrodomésticos", "Atualizado")
        );

        CategoriaRecord resultado = service.atualizar(1L, novaCategoriaRecord);

        assertEquals("Eletrodomésticos", resultado.nome());
        assertEquals("Atualizado", resultado.descricao());
        verify(repository).save(any(Categoria.class));
    }

    @Test
    void deveLancarExcecaoAoAtualizarCategoriaInexistente() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> service.atualizar(99L, categoriaRecord)
        );

        assertEquals("Categoria não encontrada", exception.getMessage());
    }

    @Test
    void deveDeletarCategoriaComSucesso() {
        when(repository.findById(1L)).thenReturn(Optional.of(categoria));

        service.deletar(1L);

        verify(repository, times(1)).delete(categoria);
    }

    @Test
    void deveLancarExcecaoAoDeletarCategoriaInexistente() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> service.deletar(99L)
        );

        assertEquals("Categoria não encontrada", exception.getMessage());
        verify(repository, never()).delete(any());
    }
}

