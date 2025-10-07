package com.gerenciador.auth.mapper;

import com.gerenciador.auth.entity.Usuario;
import com.gerenciador.auth.record.RegisterRequestRecord;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    @Mapping(target = "password", ignore = true)
    Usuario toUsuario (RegisterRequestRecord registerRequestRecord);
}
