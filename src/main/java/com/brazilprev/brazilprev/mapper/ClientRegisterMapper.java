package com.brazilprev.brazilprev.mapper;

import com.brazilprev.brazilprev.api.request.ClientRegisterRequest;
import com.brazilprev.brazilprev.api.response.ClientRegisterResponse;
import com.brazilprev.brazilprev.model.domain.City;
import com.brazilprev.brazilprev.model.domain.ClientRegister;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ClientRegisterMapper {

    @Autowired
    private ModelMapper mapper;

    public ClientRegisterResponse fromClientRegisterToClientRegisterResponse(ClientRegister clientRegister) {
        return mapper.map(clientRegister, ClientRegisterResponse.class);
    }

    public ClientRegister fromClientRegisterRequestToClientRegister(ClientRegisterRequest clientRegisterRequest) {
        return mapper.map(clientRegisterRequest, ClientRegister.class);
    }

    public void fromClientRegisterRequestToClientRegister(ClientRegisterRequest clientRegisterRequest, ClientRegister clientRegister) {
        if (clientRegister.getAddress() != null) {
            clientRegister.getAddress().setCity(new City());
        }
        mapper.map(clientRegisterRequest, clientRegister);
    }

    public List<ClientRegisterResponse> fromListClientRegisterToListClientRegisterResponse(List<ClientRegister> clientRegisters) {
        return clientRegisters.stream()
                .map(clientRegister -> fromClientRegisterToClientRegisterResponse( clientRegister))
                .collect(Collectors.toList());
    }

}
