package com.gerenciador.produto.repository.custom.impl;

import com.gerenciador.produto.entity.Produto;
import com.gerenciador.produto.repository.custom.ProdutoRepositoryCustom;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class ProdutoRepositoryImpl implements ProdutoRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    public Page<Produto> buscarPorFiltros( String nome, String descricao, BigDecimal preco, Long idCategoria, Integer quantidadeEstoque, Pageable pageable) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery<Produto> cq = cb.createQuery(Produto.class);
        Root<Produto> root = cq.from(Produto.class);

        List<Predicate> predicates = criarPredicados(cb, root, nome, descricao, preco, idCategoria, quantidadeEstoque);
        cq.where(predicates.toArray(Predicate[]::new));

        if (pageable.getSort().isSorted()) {
            cq.orderBy(pageable.getSort().stream()
                    .map(order -> order.isAscending()
                            ? cb.asc(root.get(order.getProperty()))
                            : cb.desc(root.get(order.getProperty())))
                    .toList());
        }

        TypedQuery<Produto> query = entityManager.createQuery(cq)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize());

        List<Produto> resultados = query.getResultList();

        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Produto> countRoot = countQuery.from(Produto.class);
        countQuery.select(cb.count(countRoot))
                .where(criarPredicados(cb, countRoot, nome, descricao, preco, idCategoria, quantidadeEstoque).toArray(Predicate[]::new));

        Long total = entityManager.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(resultados, pageable, total);
    }

    private List<Predicate> criarPredicados(CriteriaBuilder cb, Root<Produto> root, String nome, String descricao, BigDecimal preco, Long idCategoria, Integer quantidadeEstoque) {

        List<Predicate> predicates = new ArrayList<>();

        Optional.ofNullable(nome)
                .filter(s -> !s.isBlank())
                .ifPresent(s -> predicates.add(cb.like(cb.lower(root.get("nome")), "%" + s.toLowerCase() + "%")));

        Optional.ofNullable(descricao)
                .filter(s -> !s.isBlank())
                .ifPresent(s -> predicates.add(cb.like(cb.lower(root.get("descricao")), "%" + s.toLowerCase() + "%")));

        Optional.ofNullable(preco)
                .ifPresent(p -> predicates.add(cb.equal(root.get("preco"), p)));

        Optional.ofNullable(idCategoria)
                .ifPresent(id -> predicates.add(cb.equal(root.get("categoria").get("id"), id)));

        Optional.ofNullable(quantidadeEstoque)
                .ifPresent(q -> predicates.add(cb.equal(root.get("quantidadeEstoque"), q)));

        return predicates;
    }
}
