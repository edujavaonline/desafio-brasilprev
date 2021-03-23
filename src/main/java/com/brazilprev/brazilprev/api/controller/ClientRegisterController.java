package com.brazilprev.brazilprev.api.controller;

import com.brazilprev.brazilprev.api.openapi.ClientRegisterControllerOpenApi;
import com.brazilprev.brazilprev.api.request.ClientRegisterRequest;
import com.brazilprev.brazilprev.api.response.ClientRegisterResponse;
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

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_SEARCH_CLIENT') and #oauth2.hasScope('read')")
    public List<ClientRegisterResponse> findAll(@RequestParam(required = false) boolean showInactivesToo) {
        return clientRegisterService.findAll(showInactivesToo);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_SEARCH_CLIENT') and #oauth2.hasScope('read')")
    public ClientRegisterResponse findById(@PathVariable Long id) {
        return clientRegisterService.findOne(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ROLE_REGISTER_CLIENT') and #oauth2.hasScope('write')")
    public ClientRegisterResponse save(@Valid @RequestBody ClientRegisterRequest clientRegisterRequest) {
        return clientRegisterService.save(clientRegisterRequest);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_REGISTER_CLIENT') and #oauth2.hasScope('write')")
    public ClientRegisterResponse update(@PathVariable Long id, @RequestBody @Valid ClientRegisterRequest clientRegisterRequest) {
        return clientRegisterService.update(id, clientRegisterRequest);
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
