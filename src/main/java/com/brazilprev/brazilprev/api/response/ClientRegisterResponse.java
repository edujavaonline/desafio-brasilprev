package com.brazilprev.brazilprev.api.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ClientRegisterResponse {

    @ApiModelProperty(example = "1")
    private Long id;

    @ApiModelProperty(example = "Eduardo dos Reis Santos")
    private String name;

    @ApiModelProperty(example = "22233344455", required = true)
    private String cpf;

    @ApiModelProperty(example = "Masculino")
    private String gender;

    private LocalDate birthDate;

    private AddressResponse address;

    private Boolean active;
}
