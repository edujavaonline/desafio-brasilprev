package com.brazilprev.brazilprev.model.service;

import com.brazilprev.brazilprev.api.response.CityResponse;
import com.brazilprev.brazilprev.api.response.CitySummaryResponse;
import com.brazilprev.brazilprev.exception.EntityNotFoundException;
import com.brazilprev.brazilprev.mapper.CityMapper;
import com.brazilprev.brazilprev.model.domain.City;
import com.brazilprev.brazilprev.model.repository.CityRepository;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CityService {

    private final CityRepository cityRepository;

    private final MessageSource messageSource;

    private final CityMapper cityMapper;

    public CityService(CityRepository cityRepository, MessageSource messageSource, CityMapper cityMapper) {
        this.cityRepository = cityRepository;
        this.messageSource = messageSource;
        this.cityMapper = cityMapper;
    }

    public City findById(Long id) {
        return cityRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(messageSource.getMessage("city.not.found", null, LocaleContextHolder.getLocale())));
    }

    public CityResponse findOne(Long id) {
        return cityMapper.fromCityToCityResponse(findById(id));
    }

    public List<CitySummaryResponse> findByStateId(Long stateId) {
        return cityMapper.fromListCityToListCitySummaryResponse(cityRepository.findByStateId(stateId));
    }
}
