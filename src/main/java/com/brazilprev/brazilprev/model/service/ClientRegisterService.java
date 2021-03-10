package com.brazilprev.brazilprev.model.service;

import com.brazilprev.brazilprev.exception.BusinessException;
import com.brazilprev.brazilprev.exception.EntityNotFoundException;
import com.brazilprev.brazilprev.model.domain.City;
import com.brazilprev.brazilprev.model.domain.ClientRegister;
import com.brazilprev.brazilprev.model.repository.ClientRegisterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ClientRegisterService {

    @Autowired
    private ClientRegisterRepository clientRegisterRepository;

    @Autowired
    private CityService cityService;

    @Autowired
    private MessageSource messageSource;

    public List<ClientRegister> findAll() {
        return clientRegisterRepository.findAll();
    }

    public List<ClientRegister> findAllActives() {
        return clientRegisterRepository.findAllActives();
    }

    public ClientRegister findById(Long id) {
        return clientRegisterRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(messageSource.getMessage("client-register.not.found", null, LocaleContextHolder.getLocale())));
    }

    @Transactional
    public ClientRegister save(ClientRegister clientRegister) {
        clientRegisterRepository.detach(clientRegister);
        validateClientRegister(clientRegister);
        return clientRegisterRepository.save(clientRegister);
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

    private void validateClientRegister(ClientRegister clientRegister) {
        City city = cityService.findById(clientRegister.getAddress().getCity().getId());
        clientRegister.getAddress().setCity(city);

        Optional<ClientRegister> existingCpf = clientRegisterRepository.findByCpf(clientRegister.getCpf());

        if (existingCpf.isPresent() && !existingCpf.get().equals(clientRegister)) {
            throw new BusinessException(String.format(messageSource.getMessage("cpf.existing", null, LocaleContextHolder.getLocale()), clientRegister.getCpf()));
        }
    }
}
