package com.brazilprev.brazilprev.api.openapi;

import com.brazilprev.brazilprev.api.response.CityResponse;
import com.brazilprev.brazilprev.api.response.CitySummaryResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.List;

@Api(tags = "Cities")
public interface CityControllerOpenApi {

    @ApiOperation("Find city by id")
    public CityResponse findById(Long id);

    @ApiOperation("Find all cities by state id")
    public List<CitySummaryResponse> findAllByState(Long stateId);
}
