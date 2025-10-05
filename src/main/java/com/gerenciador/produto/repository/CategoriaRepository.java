package com.gerenciador.produto.repository;

import com.gerenciador.produto.entity.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    @Query(value = "SELECT * FROM categoria " +
            "WHERE (:nome IS NULL OR LOWER(nome) LIKE CONCAT('%', LOWER(:nome), '%')) " +
            "AND (:descricao IS NULL OR LOWER(descricao) LIKE CONCAT('%', LOWER(:descricao), '%'))",
            nativeQuery = true)
    List<Categoria> buscarPorNomeOuDescricao(@Param("nome") String nome,
                                             @Param("descricao") String descricao);

}
