package com.brazilprev.brazilprev.service;

import com.brazilprev.brazilprev.api.request.AddressRequest;
import com.brazilprev.brazilprev.api.request.CityIdRequest;
import com.brazilprev.brazilprev.api.request.ClientRegisterRequest;
import com.brazilprev.brazilprev.api.response.AddressResponse;
import com.brazilprev.brazilprev.api.response.ClientRegisterResponse;
import com.brazilprev.brazilprev.exception.BusinessException;
import com.brazilprev.brazilprev.exception.EntityNotFoundException;
import com.brazilprev.brazilprev.mapper.ClientRegisterMapper;
import com.brazilprev.brazilprev.model.domain.Address;
import com.brazilprev.brazilprev.model.domain.City;
import com.brazilprev.brazilprev.model.domain.ClientRegister;
import com.brazilprev.brazilprev.model.domain.State;
import com.brazilprev.brazilprev.model.repository.ClientRegisterRepository;
import com.brazilprev.brazilprev.model.service.CityService;
import com.brazilprev.brazilprev.model.service.ClientRegisterService;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClientRegisterServiceTest {

    @Mock
    private ClientRegisterRepository clientRegisterRepository;

    @Mock
    private CityService cityService;

    @Mock
    private MessageSource messageSource;

    @Mock
    private ClientRegisterMapper clientRegisterMapper;

    @InjectMocks
    private ClientRegisterService clientRegisterService;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        clientRegisterService = new ClientRegisterService(clientRegisterRepository, cityService, messageSource, clientRegisterMapper);
    }

    @Test
    public void shouldFindAll() {
        when(clientRegisterRepository.findAll()).thenReturn(getListRegister());
        when(clientRegisterMapper.fromListClientRegisterToListClientRegisterResponse(getListRegister())).thenReturn(getListResponse());

        List<ClientRegisterResponse> listClientRegisterResponse = clientRegisterService.findAll(true);

        assertEquals(getListResponse(), listClientRegisterResponse);
        verify(clientRegisterRepository, only()).findAll();
    }

    @Test
    public void shouldFindAllActives() {
        when(clientRegisterRepository.findAllActives()).thenReturn(getListRegister());
        when(clientRegisterMapper.fromListClientRegisterToListClientRegisterResponse(getListRegister())).thenReturn(getListResponse());

        List<ClientRegisterResponse> listClientRegisterResponse = clientRegisterService.findAll(false);

        assertEquals(getListResponse(), listClientRegisterResponse);
        verify(clientRegisterRepository, only()).findAllActives();
    }

    @Test
    public void shouldFindById() {
        when(clientRegisterRepository.findById(any())).thenReturn(Optional.of(getClientRegisterMock()));
        when(messageSource.getMessage(any(), any(), any())).thenReturn(new String());

        ClientRegister clientRegister = clientRegisterService.findById(1l);

        assertEquals(1l, clientRegister.getId().longValue());
    }

    @Test
    public void shouldFindOne() {
        when(clientRegisterRepository.findById(any())).thenReturn(Optional.of(getClientRegisterMock()));
        when(clientRegisterMapper.fromClientRegisterToClientRegisterResponse(any())).thenReturn(getClientRegisterResponseMock());
        when(messageSource.getMessage(any(), any(), any())).thenReturn(new String());

        ClientRegisterResponse clientRegisterResponse = clientRegisterService.findOne(1l);

        assertEquals(1l, clientRegisterResponse.getId().longValue());
    }

    @Test(expected = EntityNotFoundException.class)
    public void shouldNotFindOne() {
        when(clientRegisterRepository.findById(any())).thenReturn(Optional.empty());
        when(messageSource.getMessage(any(), any(), any())).thenReturn(new String());
        clientRegisterService.findOne(1l);
    }

    @Test
    public void shouldSaveSuccess() {
        when(clientRegisterMapper.fromClientRegisterRequestToClientRegister(getClientRegisterRequestMock())).thenReturn(getClientRegisterMock());
        when(clientRegisterRepository.findByCpf(any())).thenReturn(Optional.empty());
        when(clientRegisterRepository.save(getClientRegisterMock())).thenReturn(getClientRegisterMock());
        when(clientRegisterMapper.fromClientRegisterToClientRegisterResponse(getClientRegisterMock())).thenReturn(getClientRegisterResponseMock());
        ClientRegisterResponse clientRegisterResponse = clientRegisterService.save(getClientRegisterRequestMock());

        assertNotEquals("11122233344", clientRegisterResponse.getCpf());
        verify(cityService).findById(getClientRegisterMock().getAddress().getCity().getId());
        verify(clientRegisterRepository).findByCpf(getClientRegisterMock().getCpf());
    }

    @Test(expected = BusinessException.class)
    public void shouldSaveErrorCpfDuplicate() {
        when(clientRegisterMapper.fromClientRegisterRequestToClientRegister(getClientRegisterRequestMock())).thenReturn(getClientRegisterMock());
        when(clientRegisterRepository.findByCpf(any())).thenReturn(Optional.of(getClientRegisterMock2()));
        when(messageSource.getMessage(any(), any(), any())).thenReturn(new String());

        clientRegisterService.save(getClientRegisterRequestMock());
    }

    @Test(expected = BusinessException.class)
    public void shouldSaveErrorCityNotFound() {
        when(clientRegisterMapper.fromClientRegisterRequestToClientRegister(getClientRegisterRequestMock())).thenReturn(getClientRegisterMock());
        when(clientRegisterRepository.findByCpf(any())).thenReturn(Optional.of(getClientRegisterMock()));
        when(cityService.findById(any())).thenThrow(EntityNotFoundException.class);
        when(messageSource.getMessage(any(), any(), any())).thenReturn(new String());

        clientRegisterService.save(getClientRegisterRequestMock());
    }

    @Test
    public void shouldUpdateSuccess() {
        when(clientRegisterRepository.findById(any())).thenReturn(Optional.of(getClientRegisterMock()));
        when(clientRegisterRepository.findByCpf(any())).thenReturn(Optional.of(getClientRegisterMock()));
        when(clientRegisterMapper.fromClientRegisterRequestToClientRegister(getClientRegisterRequestMock())).thenReturn(getClientRegisterMock());
        when(clientRegisterRepository.save(getClientRegisterMock())).thenReturn(getClientRegisterMock());
        when(clientRegisterMapper.fromClientRegisterToClientRegisterResponse(getClientRegisterMock())).thenReturn(getClientRegisterResponseMock());

        ClientRegisterResponse clientRegisterResponse = clientRegisterService.update(1l, getClientRegisterRequestMock());

        assertNotEquals("11122233344", clientRegisterResponse.getCpf());
        verify(cityService).findById(getClientRegisterMock().getAddress().getCity().getId());
        verify(clientRegisterRepository).findByCpf(getClientRegisterMock().getCpf());
    }

    @Test(expected = BusinessException.class)
    public void shouldUpdateErrorCityNotFound() {
        when(clientRegisterRepository.findById(any())).thenReturn(Optional.of(getClientRegisterMock()));
        when(clientRegisterRepository.findByCpf(any())).thenReturn(Optional.of(getClientRegisterMock()));
        when(cityService.findById(any())).thenThrow(EntityNotFoundException.class);

        clientRegisterService.update(1l, getClientRegisterRequestMock());
    }



    private ClientRegister getClientRegisterMock() {
        State state = new State();
        state.setId(1l);
        state.setName("São Paulo");

        City city = new City();
        city.setId(1l);
        city.setName("Taquaritinga");
        city.setState(state);

        Address address = new Address();
        address.setNeighborhood("Centro");
        address.setNumber("11");
        address.setStreetName("Prudente de Morais");
        address.setZipCode("12345678");
        address.setCity(city);

        ClientRegister clientRegister = new ClientRegister();
        clientRegister.setCpf("22233344455");
        clientRegister.setGender("Masculino");
        clientRegister.setName("Fernando");
        clientRegister.setBirthDate(LocalDate.of(2000, 10, 13 ));
        clientRegister.setAddress(address);
        clientRegister.setActive(true);
        clientRegister.setId(1l);
        return clientRegister;
    }

    private ClientRegister getClientRegisterMock2() {
        State state = new State();
        state.setId(1l);
        state.setName("São Paulo");

        City city = new City();
        city.setId(1l);
        city.setName("Taquaritinga");
        city.setState(state);

        Address address = new Address();
        address.setNeighborhood("Centro");
        address.setNumber("11");
        address.setStreetName("Prudente de Morais");
        address.setZipCode("12345678");
        address.setCity(city);

        ClientRegister clientRegister = new ClientRegister();
        clientRegister.setCpf("22233344455");
        clientRegister.setGender("Masculino");
        clientRegister.setName("Fernando");
        clientRegister.setBirthDate(LocalDate.of(2000, 10, 13 ));
        clientRegister.setAddress(address);
        clientRegister.setActive(true);
        clientRegister.setId(2l);
        return clientRegister;
    }

    private List<ClientRegister> getListRegister() {
        List<ClientRegister> clientRegisters = new ArrayList<>();
        clientRegisters.add(getClientRegisterMock());
        return clientRegisters;
    }

    private List<ClientRegisterResponse> getListResponse() {
        List<ClientRegisterResponse> clientRegistersRegisterResponses = new ArrayList<>();
        clientRegistersRegisterResponses.add(getClientRegisterResponseMock());
        return clientRegistersRegisterResponses;
    }

    private ClientRegisterResponse getClientRegisterResponseMock() {
        AddressResponse addressResponse = new AddressResponse();
        addressResponse.setStateName("São Paulo");
        addressResponse.setCityName("Taquaritinga");
        addressResponse.setNeighborhood("Centro");
        addressResponse.setNumber("11");
        addressResponse.setStreetName("Prudente de Morais");
        addressResponse.setZipCode("12345678");

        ClientRegisterResponse clientRegisterResponse = new ClientRegisterResponse();
        clientRegisterResponse.setId(1l);
        clientRegisterResponse.setCpf("22233344455");
        clientRegisterResponse.setGender("Masculino");
        clientRegisterResponse.setName("Fernando");
        clientRegisterResponse.setBirthDate(LocalDate.of(2000, 10, 13 ));
        clientRegisterResponse.setActive(true);
        clientRegisterResponse.setAddress(addressResponse);
        return clientRegisterResponse;
    }

    private ClientRegisterRequest getClientRegisterRequestMock() {
        CityIdRequest cityIdRequest = new CityIdRequest();
        cityIdRequest.setId(1l);

        AddressRequest addressRequest = new AddressRequest();
        addressRequest.setCity(cityIdRequest);
        addressRequest.setNeighborhood("Centro");
        addressRequest.setNumber("11");
        addressRequest.setStreetName("Prudente de Morais");
        addressRequest.setZipCode("12345678");

        ClientRegisterRequest clientRegisterRequest = new ClientRegisterRequest();
        clientRegisterRequest.setCpf("22233344455");
        clientRegisterRequest.setGender("Masculino");
        clientRegisterRequest.setName("Fernando");
        clientRegisterRequest.setBirthDate(LocalDate.of(2000, 10, 13 ));
        clientRegisterRequest.setAddress(addressRequest);
        return clientRegisterRequest;
    }
}
