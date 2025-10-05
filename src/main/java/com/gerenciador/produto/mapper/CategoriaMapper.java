package com.gerenciador.produto.mapper;

import com.gerenciador.produto.entity.Categoria;
import com.gerenciador.produto.record.CategoriaRecord;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoriaMapper {


    CategoriaRecord toRecord(Categoria categoria);

    @Mapping(target = "id",  ignore = true)
    Categoria toSaveEntity(CategoriaRecord record);

    List<CategoriaRecord> toRecordList(List<Categoria> categorias);

}

