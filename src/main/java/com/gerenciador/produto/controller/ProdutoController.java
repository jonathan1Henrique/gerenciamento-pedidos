package com.gerenciador.produto.controller;

import com.gerenciador.paginacao.PageRecord;
import com.gerenciador.produto.record.ProdutoRecord;
import com.gerenciador.produto.service.ProdutoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/produtos")
@RequiredArgsConstructor
@Tag(name = "Produtos", description = "Operações relacionadas a produtos")
public class ProdutoController {

    private final ProdutoService service;

    @PostMapping
    @Operation(summary = "Criar novo produto")
    public ResponseEntity<ProdutoRecord> criar(@RequestBody @Valid ProdutoRecord record) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.salvar(record));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar produto existente")
    public ResponseEntity<ProdutoRecord> atualizar(@PathVariable Long id, @RequestBody ProdutoRecord record) {
        return ResponseEntity.ok(service.atualizar(id, record));
    }

    @GetMapping
    @Operation(summary = "Listar todos os produtos")
    public ResponseEntity<PageRecord> listar(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String descricao,
            @RequestParam(required = false) BigDecimal preco,
            @RequestParam(required = false) Long idCategoria,
            @RequestParam(required = false) Integer quantidadeEstoque,
            @PageableDefault(size = 10) Pageable pageable
    ) {
        return ResponseEntity.ok(service.listar(nome,descricao, preco, idCategoria, quantidadeEstoque, pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar produto por ID")
    public ResponseEntity<ProdutoRecord> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir produto por ID")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
