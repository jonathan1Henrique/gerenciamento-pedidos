package com.gerenciador.produto.service.impl;

import com.gerenciador.exception.NotFoundException;
import com.gerenciador.produto.entity.Categoria;
import com.gerenciador.produto.mapper.CategoriaMapper;
import com.gerenciador.produto.record.CategoriaRecord;
import com.gerenciador.produto.repository.CategoriaRepository;
import com.gerenciador.produto.service.CategoriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoriaServiceImpl implements CategoriaService {

    private final CategoriaRepository repository;
    private final CategoriaMapper mapper;
    private static final String CATEGORIA_NAO_ENCONTRADA = "Categoria não encontrada";

    @Override
    public CategoriaRecord salvar(CategoriaRecord categoriaRecord) {
        Categoria categoria = mapper.toSaveEntity(categoriaRecord);
        return mapper.toRecord(repository.save(categoria));
    }

    @Override
    public List<CategoriaRecord> listar(String nome, String descricao) {
        List<Categoria> categoriaPage = repository.buscarPorNomeOuDescricao(nome, descricao);
        if (categoriaPage.isEmpty()) {
            throw new NotFoundException("Nenhuma categoria encontrada com os critérios informados.");
        }
        return mapper.toRecordList(categoriaPage);
    }

    @Override
    public CategoriaRecord buscarPorId(Long id) {
        return mapper.toRecord(repository.findById(id).orElse(null));
    }

    @Override
    public CategoriaRecord atualizar(Long id, CategoriaRecord categoriaRecord) {
        return mapper.toRecord(repository.findById(id)
                .map(categoria -> {
                    categoria.setNome(categoriaRecord.nome());
                    categoria.setDescricao(categoriaRecord.descricao());
                    return repository.save(categoria);
                })
                .orElseThrow(() -> new NotFoundException(CATEGORIA_NAO_ENCONTRADA)));
    }

    @Override
    public void deletar(Long id) {
        Categoria categoria = repository.findById(id)
                .orElseThrow(() -> new NotFoundException(CATEGORIA_NAO_ENCONTRADA));
        repository.delete(categoria);
    }
}
