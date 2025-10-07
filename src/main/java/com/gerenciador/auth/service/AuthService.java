package com.gerenciador.auth.service;


import com.gerenciador.auth.record.LoginResponseRecord;
import com.gerenciador.auth.record.LoginResquetRecord;
import com.gerenciador.auth.record.RegisterRequestRecord;
import com.gerenciador.auth.record.UsuarioLogadoRecord;

public interface AuthService {

    void register(RegisterRequestRecord request);
    LoginResponseRecord login(LoginResquetRecord request);
    UsuarioLogadoRecord getUsuarioLogado();
}
