package com.brazilprev.brazilprev.api.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class StateResponse {

    @ApiModelProperty(example = "1")
    private Long id;

    @ApiModelProperty(example = "São Paulo")
    private String name;
}
