package com.brazilprev.brazilprev.model.service;

import com.brazilprev.brazilprev.api.request.ClientRegisterRequest;
import com.brazilprev.brazilprev.api.response.ClientRegisterResponse;
import com.brazilprev.brazilprev.exception.BusinessException;
import com.brazilprev.brazilprev.exception.EntityNotFoundException;
import com.brazilprev.brazilprev.mapper.ClientRegisterMapper;
import com.brazilprev.brazilprev.model.domain.City;
import com.brazilprev.brazilprev.model.domain.ClientRegister;
import com.brazilprev.brazilprev.model.repository.ClientRegisterRepository;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ClientRegisterService {


    private final ClientRegisterRepository clientRegisterRepository;

    private final CityService cityService;

    private final MessageSource messageSource;

    private final ClientRegisterMapper clientRegisterMapper;

    public ClientRegisterService(ClientRegisterRepository clientRegisterRepository, CityService cityService, MessageSource messageSource, ClientRegisterMapper clientRegisterMapper) {
        this.clientRegisterRepository = clientRegisterRepository;
        this.cityService = cityService;
        this.messageSource = messageSource;
        this.clientRegisterMapper = clientRegisterMapper;
    }

    public List<ClientRegisterResponse> findAll(boolean showInactivesToo) {
        List<ClientRegister> clientRegisters = null;
        if (showInactivesToo) {
            clientRegisters =  clientRegisterRepository.findAll();
        } else {
            clientRegisters = clientRegisterRepository.findAllActives();
        }
        return clientRegisterMapper.fromListClientRegisterToListClientRegisterResponse(clientRegisters);
    }

    public ClientRegister findById(Long id) {
        return clientRegisterRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(messageSource.getMessage("client-register.not.found", null, LocaleContextHolder.getLocale())));
    }

    public ClientRegisterResponse findOne(Long id) {
        return clientRegisterMapper.fromClientRegisterToClientRegisterResponse(findById(id));
    }

    @Transactional
    public ClientRegisterResponse save(ClientRegisterRequest clientRegisterRequest) {
        try {
            ClientRegister clientRegister = clientRegisterMapper.fromClientRegisterRequestToClientRegister(clientRegisterRequest);
            validateCpfDuplicate(clientRegister.getCpf(), clientRegister.getId());
            validateCity(clientRegister);
            clientRegister = clientRegisterRepository.save(clientRegister);
            return clientRegisterMapper.fromClientRegisterToClientRegisterResponse(clientRegister);
        } catch (EntityNotFoundException e) {
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @Transactional
    public ClientRegisterResponse update(Long id, ClientRegisterRequest clientRegisterRequest) {
        try {
            ClientRegister clientRegister = findById(id);
            validateCpfDuplicate(clientRegisterRequest.getCpf(), clientRegister.getId());
            validateCity(clientRegister);
            clientRegisterMapper.fromClientRegisterRequestToClientRegister(clientRegisterRequest, clientRegister);
            clientRegister = clientRegisterRepository.save(clientRegister);
            return clientRegisterMapper.fromClientRegisterToClientRegisterResponse(clientRegister);
        } catch (EntityNotFoundException e) {
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @Transactional
    public void active(Long id) {
        ClientRegister clientRegister = findById(id);
        clientRegister.active();
    }

    @Transactional
    public void inactive(Long id) {
        ClientRegister clientRegister = findById(id);
        clientRegister.inactive();
    }

    private void validateCity(ClientRegister clientRegister) {
        City city = cityService.findById(clientRegister.getAddress().getCity().getId());
        clientRegister.getAddress().setCity(city);
    }

    private void validateCpfDuplicate(String cpf, Long id) {
        Optional<ClientRegister> existingCpf = clientRegisterRepository.findByCpf(cpf);

        if (existingCpf.isPresent() && existingCpf.get().getId() != id) {
            throw new BusinessException(String.format(messageSource.getMessage("cpf.existing", null, LocaleContextHolder.getLocale()), cpf));
        }
    }
}
