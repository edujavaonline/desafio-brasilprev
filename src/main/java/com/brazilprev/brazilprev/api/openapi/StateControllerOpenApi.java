package com.brazilprev.brazilprev.api.openapi;

import com.brazilprev.brazilprev.api.response.StateResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.List;

@Api(tags = "States")
public interface StateControllerOpenApi {

    @ApiOperation("Find all states")
    public List<StateResponse> findAll() ;
}
