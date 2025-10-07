package com.gerenciador.auth.record;

import com.gerenciador.auth.enums.Role;

public record RegisterRequestRecord(String username, String password, Role role) {
}
