package com.gerenciador.produto.mapper;

import com.gerenciador.paginacao.PageRecord;
import com.gerenciador.produto.entity.Produto;
import com.gerenciador.produto.record.ProdutoRecord;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProdutoMapper {
    ProdutoRecord toRecord(Produto produto);

    @Mapping(target = "id",  ignore = true)
    Produto toSaveEntity(ProdutoRecord record);

    Produto toEntity(ProdutoRecord record);

    PageRecord toPageRecord(Page<Produto> produtoPage);

    List<ProdutoRecord> toRecordList(List<Produto> produtos);

}

