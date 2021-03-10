package com.brazilprev.brazilprev.api.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CityIdRequest {

    @ApiModelProperty(example = "1", required = true)
    @NotNull
    private Long id;
}
