package com.brazilprev.brazilprev.model.service;

import com.brazilprev.brazilprev.exception.EntityNotFoundException;
import com.brazilprev.brazilprev.model.domain.City;
import com.brazilprev.brazilprev.model.repository.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CityService {

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private MessageSource messageSource;

    public City findById(Long id) {
        return cityRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(messageSource.getMessage("city.not.found", null, LocaleContextHolder.getLocale())));
    }

    public List<City> findByStateId(Long stateId) {
        return cityRepository.findByStateId(stateId);
    }
}
