package com.gerenciador.auth.record;

import lombok.Builder;

@Builder
public record UsuarioLogadoRecord(String username, String role) {
}
