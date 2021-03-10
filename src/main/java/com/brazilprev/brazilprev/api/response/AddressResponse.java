package com.brazilprev.brazilprev.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddressResponse {

    @ApiModelProperty(example = "15900063")
    private String zipCode;

    @ApiModelProperty(example = "Rua Siqueira Campos")
    private String streetName;

    @ApiModelProperty(example = "533")
    private String number;

    @ApiModelProperty(example = "Fundos")
    private String complement;

    @ApiModelProperty(example = "Centro")
    private String neighborhood;

    @ApiModelProperty(example = "Taquaritinga")
    private String cityName;

    @ApiModelProperty(example = "São Paulo")
    private String stateName;
}
