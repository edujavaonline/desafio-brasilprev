package com.brazilprev.brazilprev.mapper;

import com.brazilprev.brazilprev.api.response.CityResponse;
import com.brazilprev.brazilprev.api.response.CitySummaryResponse;
import com.brazilprev.brazilprev.model.domain.City;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CityMapper {

    @Autowired
    private ModelMapper mapper;

    public CityResponse fromCityToCityResponse(City city) {
        return mapper.map(city, CityResponse.class);
    }

    public CitySummaryResponse fromCityToCitySummaryResponse(City city) {
        return mapper.map(city, CitySummaryResponse.class);
    }

    public List<CitySummaryResponse> fromListCityToListCitySummaryResponse(List<City> cities) {
        return cities.stream()
                .map(city -> fromCityToCitySummaryResponse(city))
                .collect(Collectors.toList());
    }
}
