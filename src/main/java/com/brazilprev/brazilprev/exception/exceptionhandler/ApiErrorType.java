package com.brazilprev.brazilprev.exception.exceptionhandler;

import lombok.Getter;

@Getter
public enum ApiErrorType {

    RESOURCE_NOT_FOUND("/recurso-nao-encontrado", "Recurso não encontrado!"),
    ENTITY_IN_USE("/entidade-em-uso", "Entidade em uso!"),
    ERROR_BUSINESS("/erro-negocio", "Violação de regra de negócio!"),
    MESSAGE_NOT_READBLE("/mensagem-incompreensivel", "Mensagem incompreensível!"),
    INVALID_PAYLOAD_PARAMETER("/parametro-payload-invalido", "Parâmetro de payload inválido!"),
    INVALID_URL_PARAMETER("/parametro-url-invalido", "Parâmetro de url inválido!"),
    INTERNAL_SERVER_ERROR("/erro-de-sistema", "Erro de Sistema!"),
    INVALID_DATA("/dados-invalidos", "Dados inválidos!");

    private String title;
    private String uri;

    ApiErrorType(String path, String title) {
        this.uri = "https://brasilprev.com.br" + path;
        this.title = title;
    }
}
