package com.brazilprev.brazilprev.configuration.modelmapper;

import com.brazilprev.brazilprev.api.response.AddressResponse;
import com.brazilprev.brazilprev.model.domain.Address;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        var modelMapper = new ModelMapper();

        var addressResponseTypeMap = modelMapper.createTypeMap(
                Address.class, AddressResponse.class);

        addressResponseTypeMap.<String>addMapping(
                addressSource -> addressSource.getCity().getState().getName(),
                (addressDestination, value) -> addressDestination.setStateName(value));

        return modelMapper;
    }
}
