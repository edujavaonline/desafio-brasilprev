package com.brazilprev.brazilprev.api.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CityResponse {

    @ApiModelProperty(example = "1")
    private Long id;

    @ApiModelProperty(example = "Taquaritinga")
    private String name;

    @ApiModelProperty(example = "São Paulo")
    private StateResponse state;
}
