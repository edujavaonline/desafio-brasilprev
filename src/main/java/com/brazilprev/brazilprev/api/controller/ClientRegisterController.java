package com.brazilprev.brazilprev.api.controller;

import com.brazilprev.brazilprev.api.openapi.ClientRegisterControllerOpenApi;
import com.brazilprev.brazilprev.api.request.ClientRegisterRequest;
import com.brazilprev.brazilprev.api.response.ClientRegisterResponse;
import com.brazilprev.brazilprev.exception.BusinessException;
import com.brazilprev.brazilprev.exception.EntityNotFoundException;
import com.brazilprev.brazilprev.mapper.ClientRegisterMapper;
import com.brazilprev.brazilprev.model.domain.ClientRegister;
import com.brazilprev.brazilprev.model.service.ClientRegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/clients")
public class ClientRegisterController implements ClientRegisterControllerOpenApi {

    @Autowired
    private ClientRegisterService clientRegisterService;

    @Autowired
    private ClientRegisterMapper clientRegisterMapper;

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_SEARCH_CLIENT') and #oauth2.hasScope('read')")
    public List<ClientRegisterResponse> findAll(@RequestParam(required = false) boolean showInactivesToo) {
        List<ClientRegister> clientRegisters = null;
        if (showInactivesToo) {
            clientRegisters =  clientRegisterService.findAll();
        } else {
            clientRegisters = clientRegisterService.findAllActives();
        }
        return clientRegisterMapper.fromListClientRegisterToListClientRegisterResponse(clientRegisters);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_SEARCH_CLIENT') and #oauth2.hasScope('read')")
    public ClientRegisterResponse findById(@PathVariable Long id) {
        ClientRegister clientRegister = clientRegisterService.findById(id);
        return clientRegisterMapper.fromClientRegisterToClientRegisterResponse(clientRegister);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ROLE_REGISTER_CLIENT') and #oauth2.hasScope('write')")
    public ClientRegisterResponse save(@Valid @RequestBody ClientRegisterRequest clientRegisterRequest) {
        try {
            ClientRegister clientRegister = clientRegisterMapper.fromClientRegisterRequestToClientRegister(clientRegisterRequest);
            clientRegister = clientRegisterService.save(clientRegister);
            return clientRegisterMapper.fromClientRegisterToClientRegisterResponse(clientRegister);
        } catch (EntityNotFoundException e) {
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_REGISTER_CLIENT') and #oauth2.hasScope('write')")
    public ClientRegisterResponse update(@PathVariable Long id, @RequestBody @Valid ClientRegisterRequest clientRegisterRequest) {
        ClientRegister clientRegister = clientRegisterService.findById(id);
        clientRegisterMapper.fromClientRegisterRequestToClientRegister(clientRegisterRequest, clientRegister);
        clientRegister = clientRegisterService.save(clientRegister);
        return clientRegisterMapper.fromClientRegisterToClientRegisterResponse(clientRegister);
    }

    @PutMapping("/{id}/active")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('ROLE_ACTIVE/INACTIVE_CLIENT') and #oauth2.hasScope('write')")
    public void active(@PathVariable Long id) {
        clientRegisterService.active(id);
    }

    @PutMapping("/{id}/inactive")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('ROLE_ACTIVE/INACTIVE_CLIENT') and #oauth2.hasScope('write')")
    public void inactive(@PathVariable Long id) {
        clientRegisterService.inactive(id);
    }
}
