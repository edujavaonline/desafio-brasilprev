package com.brazilprev.brazilprev.api.controller;

import com.brazilprev.brazilprev.api.openapi.CityControllerOpenApi;
import com.brazilprev.brazilprev.api.response.CityResponse;
import com.brazilprev.brazilprev.api.response.CitySummaryResponse;
import com.brazilprev.brazilprev.mapper.CityMapper;
import com.brazilprev.brazilprev.model.domain.City;
import com.brazilprev.brazilprev.model.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/cities")
public class CityController implements CityControllerOpenApi {

    @Autowired
    private CityService cityService;

    @Autowired
    private CityMapper cityMapper;

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_SEARCH_CITY')")
    public CityResponse findById(@PathVariable Long id) {
        City city = cityService.findById(id);
        return cityMapper.fromCityToCityResponse(city);
    }

    @GetMapping()
    @PreAuthorize("hasAuthority('ROLE_SEARCH_CITY')")
    public List<CitySummaryResponse> findAllByState(@RequestParam Long stateId) {
        List<City> cities = cityService.findByStateId(stateId);
        return cityMapper.fromListCityToListCitySummaryResponse(cities);
    }
}
