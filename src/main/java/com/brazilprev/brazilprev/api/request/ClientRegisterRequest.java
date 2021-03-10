package com.brazilprev.brazilprev.api.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.br.CPF;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class ClientRegisterRequest {

    @ApiModelProperty(example = "Eduardo dos Reis Santos", required = true)
    @NotBlank
    private String name;

    @ApiModelProperty(example = "22233344455", required = true)
    @NotBlank
    @CPF
    private String cpf;

    @ApiModelProperty(example = "Masculino")
    private String gender;

    @ApiModelProperty(required = true)
    @NotNull
    private LocalDate birthDate;

    @Valid
    @NotNull
    private AddressRequest address;

}
