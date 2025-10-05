package com.gerenciador.produto.service;

import com.gerenciador.produto.record.CategoriaRecord;

import java.util.List;

public interface CategoriaService {

    CategoriaRecord salvar(CategoriaRecord categoriaRecord);
    List<CategoriaRecord> listar(String nome, String descricao);
    CategoriaRecord buscarPorId(Long id);
    CategoriaRecord atualizar(Long id, CategoriaRecord categoriaRecord);
    void deletar(Long id);
}
