package com.gerenciador.handler;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErroResponse {

    private int status;
    private String mensagem;
    private Map<String, String> erros;

    public ErroResponse(int status, String mensagem) {
        this.status = status;
        this.mensagem = mensagem;
    }

    public ErroResponse(int status, Map<String, String> erros) {
        this.status = status;
        this.erros = erros;
    }

}
