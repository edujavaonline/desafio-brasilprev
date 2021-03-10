package com.brazilprev.brazilprev.api.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class AddressRequest {

    @ApiModelProperty(example = "15900063", required = true)
    @NotBlank
    private String zipCode;

    @ApiModelProperty(example = "Rua Siqueira Campos", required = true)
    @NotBlank
    private String streetName;

    @ApiModelProperty(example = "533", required = true)
    @NotBlank
    private String number;

    @ApiModelProperty(example = "Fundos")
    private String complement;

    @ApiModelProperty(example = "Centro", required = true)
    @NotBlank
    private String neighborhood;

    @Valid
    @NotNull
    private CityIdRequest city;
}
