package com.gerenciador.produto.controller;

import com.gerenciador.produto.record.CategoriaRecord;
import com.gerenciador.produto.service.CategoriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categorias")
@RequiredArgsConstructor
@Tag(name = "Categorias", description = "Operações relacionadas a categorias de produtos")
public class CategoriaController {

    private final CategoriaService service;
    @PostMapping
    @Operation(summary = "Salva uma categoria")
    public ResponseEntity<CategoriaRecord> salvar(@RequestBody @Valid CategoriaRecord categoriaRecord){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.salvar(categoriaRecord)) ;
    }

    @PutMapping("/{id}")
    @Operation(summary = "Edita uma categoria")
    public ResponseEntity<CategoriaRecord> editar(@PathVariable Long id, @RequestBody @Valid CategoriaRecord categoriaRecord){
        return ResponseEntity.ok(service.atualizar(id, categoriaRecord));
    }

    @GetMapping
    @Operation(summary = "Lista as categorias de acordo com os filtros informados")
    public ResponseEntity<List<CategoriaRecord>> listar(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String descricao
    ) {
        return ResponseEntity.ok(service.listar(nome, descricao)) ;
    }

    @DeleteMapping
    @Operation(summary = "Deleta categoria")
    public ResponseEntity<Void> deletar(@RequestParam Long id){
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
