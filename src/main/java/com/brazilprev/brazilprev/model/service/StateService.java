package com.brazilprev.brazilprev.model.service;

import com.brazilprev.brazilprev.api.response.StateResponse;
import com.brazilprev.brazilprev.mapper.StateMapper;
import com.brazilprev.brazilprev.model.repository.StateRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StateService {

    private final StateRepository stateRepository;

    private final StateMapper stateMapper;

    public StateService(StateRepository stateRepository, StateMapper stateMapper) {
        this.stateRepository = stateRepository;
        this.stateMapper = stateMapper;
    }

    public List<StateResponse> findAll() {
        return stateMapper.fromListStateToListStateResponse(stateRepository.findAll());
    }
}
