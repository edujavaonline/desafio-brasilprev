package com.brazilprev.brazilprev.exceptionhandler;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.List;

@ApiModel("ApiError")
@JsonInclude(Include.NON_NULL)
@Getter
@Builder
public class ApiError {

    @ApiModelProperty(example = "400")
    private Integer status;

    @ApiModelProperty(example = "https://https://brasilprev.com.br/dados-invalidos")
    private String type;

    @ApiModelProperty(example = "Dados inválidos!")
    private String title;

    @ApiModelProperty(example = "Um ou mais campos estão inválidos. Faça o preenchimento correto e tente novamente!")
    private String detail;

    @ApiModelProperty(example = "Um ou mais campos estão inválidos. Faça o preenchimento correto e tente novamente!")
    private String userDetail;

    private OffsetDateTime timeStamp;

    @ApiModelProperty(value = "Lista de campos que geraram o erro (opcional)!")
    private List<Field> fields;

    @ApiModel("ApiErrorField")
    @Getter
    @Builder
    public static class Field {

        @ApiModelProperty(example = "zipCode")
        private String name;

        @ApiModelProperty(example = "Cep é obrigatório!")
        private String userMessage;
    }
}
