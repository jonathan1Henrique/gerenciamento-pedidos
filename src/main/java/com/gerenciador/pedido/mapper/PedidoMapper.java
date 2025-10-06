package com.gerenciador.pedido.mapper;

import com.gerenciador.paginacao.PageRecord;
import com.gerenciador.pedido.entity.Pedido;
import com.gerenciador.pedido.record.PedidoRecord;
import com.gerenciador.pedido.record.PedidoResumoRecord;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring")
public interface PedidoMapper {

    @Mapping(source = "status", target = "statusPedido")
    PedidoRecord toRecord(Pedido pedido);

    PageRecord toPageRecord(Page<PedidoResumoRecord> pedidoRecord);
}
