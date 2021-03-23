package com.brazilprev.brazilprev.service;

import com.brazilprev.brazilprev.api.response.CityResponse;
import com.brazilprev.brazilprev.api.response.CitySummaryResponse;
import com.brazilprev.brazilprev.api.response.StateResponse;
import com.brazilprev.brazilprev.exception.EntityNotFoundException;
import com.brazilprev.brazilprev.mapper.CityMapper;
import com.brazilprev.brazilprev.model.domain.City;
import com.brazilprev.brazilprev.model.domain.State;
import com.brazilprev.brazilprev.model.repository.CityRepository;
import com.brazilprev.brazilprev.model.service.CityService;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CityServiceTest {

    @Mock
    private CityRepository cityRepository;

    @Mock
    private MessageSource messageSource;

    @Mock
    private CityMapper cityMapper;

    @InjectMocks
    private CityService cityService;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        cityService = new CityService(cityRepository, messageSource, cityMapper);
    }

    @Test
    public void shouldFindById() {
        when(cityRepository.findById(any())).thenReturn(Optional.of(getCityMock()));
        when(messageSource.getMessage(any(), any(), any())).thenReturn(new String());

        City city = cityService.findById(1l);

        assertEquals(1l, city.getId().longValue());
    }

    @Test
    public void shouldFindOne() {
        when(cityRepository.findById(any())).thenReturn(Optional.of(getCityMock()));
        when(cityMapper.fromCityToCityResponse(getCityMock())).thenReturn(getCityResponseMock());
        when(messageSource.getMessage(any(), any(), any())).thenReturn(new String());

        CityResponse cityResponse = cityService.findOne(1l);

        assertEquals(1l, cityResponse.getId().longValue());
    }

    @Test(expected = EntityNotFoundException.class)
    public void shouldNotFindOne() {
        when(cityRepository.findById(any())).thenReturn(Optional.empty());
        when(messageSource.getMessage(any(), any(), any())).thenReturn(new String());
        cityService.findOne(1l);
    }

    @Test
    public void shouldFindByStateId() {
        when(cityRepository.findByStateId(any())).thenReturn(getListCityMock());
        when(cityMapper.fromListCityToListCitySummaryResponse(getListCityMock())).thenReturn(getListCitySummaryResponseMock());
        when(messageSource.getMessage(any(), any(), any())).thenReturn(new String());

        List<CitySummaryResponse> citiesSummaryResponse = cityService.findByStateId(1l);

        assertEquals(1l, citiesSummaryResponse.get(0).getId().longValue());
    }

    private City getCityMock() {
        State state = new State();
        state.setId(1l);
        state.setName("São Paulo");

        City city = new City();
        city.setId(1l);
        city.setName("Taquaritinga");
        city.setState(state);
        return city;
    }

    private CityResponse getCityResponseMock() {
        StateResponse stateResponse = new StateResponse();
        stateResponse.setId(1l);
        stateResponse.setName("São Paulo");

        CityResponse cityResponse = new CityResponse();
        cityResponse.setId(1l);
        cityResponse.setName("Taquaritinga");
        cityResponse.setState(stateResponse);
        return cityResponse;
    }

    private List<CitySummaryResponse> getListCitySummaryResponseMock() {
        List<CitySummaryResponse> citiesSummaryResponse = new ArrayList<>();
        citiesSummaryResponse.add(getCitySummaryResponseMock());
        return citiesSummaryResponse;
    }

    private List<City> getListCityMock() {
        List<City> cities = new ArrayList<>();
        cities.add(getCityMock());
        return cities;
    }

    private CitySummaryResponse getCitySummaryResponseMock() {
        CitySummaryResponse citySummaryResponse = new CitySummaryResponse();
        citySummaryResponse.setId(1l);
        citySummaryResponse.setName("Taquaritinga");
        return citySummaryResponse;
    }
}
