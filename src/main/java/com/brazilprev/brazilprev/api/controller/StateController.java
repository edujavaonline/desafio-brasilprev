package com.brazilprev.brazilprev.api.controller;

import com.brazilprev.brazilprev.api.response.StateResponse;
import com.brazilprev.brazilprev.mapper.StateMapper;
import com.brazilprev.brazilprev.model.service.StateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/states")
public class StateController {

    @Autowired
    private StateService stateService;

    @Autowired
    private StateMapper stateMapper;

    @GetMapping()
    @PreAuthorize("hasAuthority('ROLE_SEARCH_STATE') and #oauth2.hasScope('read')")
    public List<StateResponse> findAll() {
        return stateService.findAll();
    }
}
