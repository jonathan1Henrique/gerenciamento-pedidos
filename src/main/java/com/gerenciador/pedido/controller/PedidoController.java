package com.gerenciador.pedido.controller;

import com.gerenciador.paginacao.PageRecord;
import com.gerenciador.pedido.record.ItemRecord;
import com.gerenciador.pedido.record.PagamentoRecord;
import com.gerenciador.pedido.record.PedidoRecord;
import com.gerenciador.pedido.service.PedidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/pedidos")
@RequiredArgsConstructor
@Tag(name = "Pedidos", description = "Operações relacionadas a pedidos")
public class PedidoController {

    private final PedidoService service;

    @PostMapping
    @Operation(summary = "Cria novo Pedido")
    public ResponseEntity<PedidoRecord> criar(@RequestBody @Valid List<ItemRecord> record) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.salvar(record));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Edita um Pedido")
    public ResponseEntity<PedidoRecord> editar(@PathVariable Long id, @RequestBody @Valid List<ItemRecord> record) {
        return ResponseEntity.ok(service.editar(id, record));
    }

    @PostMapping("/pagamento/{id}")
    @Operation(summary = "Realiza pagamento de um Pedido")
    public ResponseEntity<PedidoRecord> pagamento(@PathVariable Long id, @RequestBody @Valid PagamentoRecord record) {
        return ResponseEntity.ok(service.pagamento(id, record));
    }

    @GetMapping
    @Operation(summary = "Listar Pedidos")
    public ResponseEntity<PageRecord> listar(
            @RequestParam(required = false) String usuario,
            @RequestParam(required = false) String status,
            @PageableDefault(size = 10) Pageable pageable ) {

        return ResponseEntity.ok(service.listar(usuario, status, pageable));
    }
}
