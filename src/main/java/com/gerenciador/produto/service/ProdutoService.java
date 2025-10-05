package com.gerenciador.produto.service;

import com.gerenciador.paginacao.PageRecord;
import com.gerenciador.produto.record.ProdutoRecord;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;


public interface ProdutoService {

    ProdutoRecord salvar(ProdutoRecord produtoRecord);
    PageRecord listar(String nome,
                      String descricao,
                      BigDecimal preco,
                      Long idCategoria,
                      Integer quantidadeEstoque,
                      Pageable pageable);
    ProdutoRecord buscarPorId(Long id);
    ProdutoRecord atualizar(Long id, ProdutoRecord produtoAtualizado);
    void deletar(Long id);
}
