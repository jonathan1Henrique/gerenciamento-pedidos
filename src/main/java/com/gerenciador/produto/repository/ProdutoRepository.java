package com.gerenciador.produto.repository;

import com.gerenciador.produto.entity.Produto;
import com.gerenciador.produto.repository.custom.ProdutoRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<Produto, Long>, ProdutoRepositoryCustom {

}
