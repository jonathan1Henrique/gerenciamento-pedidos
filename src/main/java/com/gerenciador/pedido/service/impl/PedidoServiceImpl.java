package com.gerenciador.pedido.service.impl;

import com.gerenciador.dashboard.record.FaturamentoMensalRecord;
import com.gerenciador.dashboard.record.TicketMedioRecord;
import com.gerenciador.dashboard.record.TopCincoRecord;
import com.gerenciador.exception.NotFoundException;
import com.gerenciador.exception.OrderNotBelongException;
import com.gerenciador.exception.OutOfStockOrderException;
import com.gerenciador.paginacao.PageRecord;
import com.gerenciador.pedido.entity.Pedido;
import com.gerenciador.pedido.entity.PedidoItens;
import com.gerenciador.pedido.enums.StatusPedido;
import com.gerenciador.pedido.mapper.PedidoMapper;
import com.gerenciador.pedido.record.ItemRecord;
import com.gerenciador.pedido.record.PagamentoRecord;
import com.gerenciador.pedido.record.PedidoRecord;
import com.gerenciador.pedido.repository.PedidoItemRepository;
import com.gerenciador.pedido.repository.PedidoRepository;
import com.gerenciador.pedido.service.PedidoService;
import com.gerenciador.produto.entity.Produto;
import com.gerenciador.produto.mapper.ProdutoMapper;
import com.gerenciador.produto.record.ProdutoRecord;
import com.gerenciador.produto.service.ProdutoService;
import com.gerenciador.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PedidoServiceImpl implements PedidoService {

    private final ProdutoService produtoService;
    private final PedidoRepository repository;
    private final PedidoItemRepository itemRepository;
    private final PedidoMapper mapper;
    private final ProdutoMapper produtoMapper;
    private final AuthService authService;

    @Override
    public PedidoRecord salvar(List<ItemRecord> itemRecords) {
        Pedido pedido = processarPedido(null, itemRecords);
        return mapper.toRecord(pedido);
    }

    @Override
    public PedidoRecord editar(Long idPedido, List<ItemRecord> itemRecords) {
        Pedido pedido = buscaPedidoPorId(idPedido);
        String username = authService.getUsuarioLogado().username();
        if (!pedido.getUsuario().equals(username)) {
            throw new OrderNotBelongException("O Pedido não pertence ao usuário logado");
        }

        List<Long> idProdutos = new ArrayList<>();
        Map<Long, Integer> quantidadeProdutos = new HashMap<>();
        itemRecords.forEach(item -> {
            idProdutos.add(item.idProduto());
            quantidadeProdutos.put(item.idProduto(), item.quantidade());
        });

        List<ProdutoRecord> produtos = produtoService.buscarPorListaId(idProdutos);

        List<PedidoItens> pedidoItens = itemRepository.findByIdPedido(pedido.getId());

        processarPedido(pedido.getId(), itemRecords);

        editaItemsPedido(produtos, pedido.getId(), quantidadeProdutos, pedidoItens);

        return mapper.toRecord(pedido);
    }

    @Override
    public PageRecord listar(String usuario, String status,
                             Pageable pageable) {

        usuario = getUsuario(usuario);

        return mapper.toPageRecord(
                repository.buscarPedidosComProdutos(
                        usuario, status,
                        pageable)
        );
    }

    private String getUsuario(String usuario) {
        String role = authService.getUsuarioLogado().role();
        if(!role.equals("ROLE_ADMIN")){
            usuario = authService.getUsuarioLogado().username();
        }
        return usuario;
    }

    @Override
    public PedidoRecord pagamento(Long id, PagamentoRecord record) {
        Pedido pedido = buscaPedidoPorId(id);
        if (!pedido.getValorTotal().equals(record.valorTotal())) {
            throw new IllegalArgumentException("o valor informado não é igual a valor total do pedido");
        }
        if (pedido.getStatus().equals(StatusPedido.CONCLUIDO)) {
            throw new IllegalArgumentException("O pedido já foi pago");
        }
        validaEstoque(id, pedido);
        pedido.setStatus(StatusPedido.CONCLUIDO);
        pedido.setDataPagamento(LocalDateTime.now());

        return mapper.toRecord(repository.save(pedido));
    }

    @Override
    public List<TopCincoRecord> topCinco() {
        return repository.buscarTopCincoUsuarios();
    }

    @Override
    public List<TicketMedioRecord> buscarTicketMedioPorUsuario() {
        return repository.buscarTicketMedioPorUsuario();
    }

    @Override
    public FaturamentoMensalRecord buscarFaturamentoMensal() {
        return repository.buscarFaturamentoMensal();
    }

    private void validaEstoque(Long id, Pedido pedido) {
        List<PedidoItens> itens = itemRepository.findByIdPedido(id);
        for (PedidoItens item : itens) {
            Produto produto = produtoMapper.toEntity(produtoService.buscarPorId(item.getIdProduto()));
            if(produto.getQuantidadeEstoque()<= 0){
                pedido.setStatus(StatusPedido.CANCELADO);
                repository.save(pedido);
                throw new OutOfStockOrderException(List.of("Quantidade de estoque insuficiente para o produto ", produto.getNome()));
            }
            produto.setQuantidadeEstoque(produto.getQuantidadeEstoque() - item.getQuantidade());
            produtoService.atualizar(produto.getId(), produtoMapper.toRecord(produto));
        }
    }

    private Pedido buscaPedidoPorId(Long idPedido) {
        return repository.findById(idPedido)
                .orElseThrow(() -> new NotFoundException("Pedido com Id " + idPedido + " não encontrado"));
    }

    private Pedido processarPedido(Long idPedido, List<ItemRecord> itemRecords) {
        List<Long> idProdutos = new ArrayList<>();
        Map<Long, Integer> quantidadeProdutos = new HashMap<>();
        itemRecords.forEach(item -> {
            idProdutos.add(item.idProduto());
            quantidadeProdutos.put(item.idProduto(), item.quantidade());
        });

        List<ProdutoRecord> produtos = produtoService.buscarPorListaId(idProdutos);
        List<String> erros = new ArrayList<>();
        Boolean semEstoque = validaEstoque(produtos, erros);
        if (!erros.isEmpty() && semEstoque) {
            throw new OutOfStockOrderException(erros);
        }
        BigDecimal valorTotal = somaValorTotal(produtos, quantidadeProdutos);

        Pedido pedido = salvaPedido(valorTotal, idPedido);
        salvaNovosItemsPedido(produtos, idPedido, quantidadeProdutos);
        return pedido;

    }

    private void salvaNovosItemsPedido(List<ProdutoRecord> produtos, Long idPedido, Map<Long, Integer> quantidadeProdutos) {
        List<PedidoItens> itens = new ArrayList<>();
        produtos.forEach(produto ->
                itens.add(PedidoItens.builder()
                        .idPedido(idPedido)
                        .idProduto(produto.id())
                        .quantidade(quantidadeProdutos.get(produto.id()))
                        .precoUnitario(produto.preco())
                        .build()
                ));
        itemRepository.saveAll(itens);
    }

    private void editaItemsPedido(
            List<ProdutoRecord> produtos,
            Long idPedido,
            Map<Long, Integer> quantidadeProdutos,
            List<PedidoItens> pedidoItensExistentes
    ) {
        Map<Long, PedidoItens> itensPorProduto = pedidoItensExistentes.stream()
                .collect(Collectors.toMap(PedidoItens::getIdProduto, item -> item));

        List<PedidoItens> itensAtualizados = new ArrayList<>();

        for (ProdutoRecord produto : produtos) {
            PedidoItens itemExistente = itensPorProduto.get(produto.id());

            if (itemExistente != null) {
                itemExistente.setQuantidade(quantidadeProdutos.get(produto.id()));
                itemExistente.setPrecoUnitario(produto.preco());
                itemExistente.setIdProduto(produto.id());
                itensAtualizados.add(itemExistente);
            } else {
                PedidoItens novoItem = PedidoItens.builder()
                        .idPedido(idPedido)
                        .idProduto(produto.id())
                        .quantidade(quantidadeProdutos.get(produto.id()))
                        .precoUnitario(produto.preco())
                        .build();
                itensAtualizados.add(novoItem);
            }
        }
        itemRepository.saveAll(itensAtualizados);
    }

    private Pedido salvaPedido(BigDecimal valorTotal, Long idPedido) {
        return repository.save(Pedido.builder()
                .id(idPedido)
                .valorTotal(valorTotal)
                .usuario(authService.getUsuarioLogado().username())
                .status(StatusPedido.PENDENTE)
                .dataPedido(LocalDateTime.now())
                .build());
    }

    private static BigDecimal somaValorTotal(List<ProdutoRecord> produtos, Map<Long, Integer> quantidadeProdutos) {
        return produtos.stream()
                .map(p -> {
                    Integer quantidade = quantidadeProdutos.getOrDefault(p.id(), 0);
                    return p.preco().multiply(BigDecimal.valueOf(quantidade));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);

    }

    private Boolean validaEstoque(List<ProdutoRecord> produtos, List<String> erros) {

        for (ProdutoRecord p : produtos) {
            if (p.quantidadeEstoque() <= 0) {
                erros.add(String.format("Quantidade de estoque insuficiente para o produto %s ", p.nome()));
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }
}
