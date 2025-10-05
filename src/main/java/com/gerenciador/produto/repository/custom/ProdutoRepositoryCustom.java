package com.gerenciador.produto.repository.custom;

import com.gerenciador.produto.entity.Produto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;

public interface ProdutoRepositoryCustom {

    Page<Produto> buscarPorFiltros(String nome, String descricao, BigDecimal preco, Long idCategoria, Integer quantidadeEstoque, Pageable pageable);
}
