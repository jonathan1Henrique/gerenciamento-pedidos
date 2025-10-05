package com.gerenciador.produto.mapper;

import com.gerenciador.produto.entity.Categoria;
import com.gerenciador.produto.record.CategoriaRecord;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-10-05T18:38:49-0300",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.13.jar, environment: Java 21.0.6 (JetBrains s.r.o.)"
)
@Component
public class CategoriaMapperImpl implements CategoriaMapper {

    @Override
    public CategoriaRecord toRecord(Categoria categoria) {
        if ( categoria == null ) {
            return null;
        }

        Long id = null;
        String nome = null;
        String descricao = null;

        id = categoria.getId();
        nome = categoria.getNome();
        descricao = categoria.getDescricao();

        CategoriaRecord categoriaRecord = new CategoriaRecord( id, nome, descricao );

        return categoriaRecord;
    }

    @Override
    public Categoria toSaveEntity(CategoriaRecord record) {
        if ( record == null ) {
            return null;
        }

        Categoria categoria = new Categoria();

        categoria.setNome( record.nome() );
        categoria.setDescricao( record.descricao() );

        return categoria;
    }

    @Override
    public List<CategoriaRecord> toRecordList(List<Categoria> categorias) {
        if ( categorias == null ) {
            return null;
        }

        List<CategoriaRecord> list = new ArrayList<CategoriaRecord>( categorias.size() );
        for ( Categoria categoria : categorias ) {
            list.add( toRecord( categoria ) );
        }

        return list;
    }
}
