package com.gerenciador.produto.service.impl;

import com.gerenciador.exception.NotFoundException;
import com.gerenciador.paginacao.PageRecord;
import com.gerenciador.produto.entity.Produto;
import com.gerenciador.produto.mapper.ProdutoMapper;
import com.gerenciador.produto.record.CategoriaRecord;
import com.gerenciador.produto.record.ProdutoRecord;
import com.gerenciador.produto.repository.ProdutoRepository;
import com.gerenciador.produto.service.CategoriaService;
import com.gerenciador.produto.service.ProdutoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ProdutoServiceImpl implements ProdutoService {

    private final ProdutoRepository repository;
    private final ProdutoMapper mapper;
    private final CategoriaService categoriaService;
    private static final String PRODUTO_NAO_ENCONTRADA = "Produto não encontrado";

    @Override
    public ProdutoRecord salvar(ProdutoRecord produtoRecord) {
        validaCategoria(produtoRecord);
        Produto produto = repository.save(mapper.toSaveEntity(produtoRecord));
        return mapper.toRecord(produto);
    }

    private void validaCategoria(ProdutoRecord produtoRecord) {
        CategoriaRecord categoriaRecord = categoriaService.buscarPorId(produtoRecord.idCategoria());
        if (categoriaRecord == null) {
            throw new NotFoundException(String.format("Nenhuma categoria encontrada com ID = %s", produtoRecord.idCategoria()));
        }
    }

    @Override
    public PageRecord listar(String nome,
                             String descricao,
                             BigDecimal preco,
                             Long idCategoria,
                             Integer quantidadeEstoque,
                             Pageable pageable) {
        Page<Produto> produtoPage = repository.buscarPorFiltros(nome, descricao, preco, idCategoria, quantidadeEstoque, pageable);

        return mapper.toPageRecord(produtoPage);
    }

    @Override
    public ProdutoRecord buscarPorId(Long id) {
        return mapper.toRecord(repository.findById(id).orElse(null));
    }

    @Override
    public List<ProdutoRecord> buscarPorListaId(List<Long> idProdutos) {
        return mapper.toRecordList(repository.findAllById(idProdutos));
    }

    @Override
    public ProdutoRecord atualizar(Long id, ProdutoRecord produtoAtualizado) {
        return mapper.toRecord(repository.findById(id)
                .map(produto -> {
                    produto.setNome(produtoAtualizado.nome());
                    produto.setDescricao(produtoAtualizado.descricao());
                    produto.setPreco(produtoAtualizado.preco());
                    produto.setIdCategoria(produtoAtualizado.idCategoria());
                    produto.setQuantidadeEstoque(produtoAtualizado.quantidadeEstoque());
                    return repository.save(produto);
                })
                .orElseThrow(() -> new NotFoundException(PRODUTO_NAO_ENCONTRADA)));
    }

    @Override
    public void deletar(Long id) {
        Produto produto = repository.findById(id)
                .orElseThrow(() -> new NotFoundException(PRODUTO_NAO_ENCONTRADA));
        repository.delete(produto);
    }
}
