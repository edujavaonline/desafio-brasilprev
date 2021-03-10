package com.brazilprev.brazilprev.model.service;

import com.brazilprev.brazilprev.model.domain.State;
import com.brazilprev.brazilprev.model.repository.StateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StateService {

    @Autowired
    private StateRepository stateRepository;

    public List<State> findAll() {
        return stateRepository.findAll();
    }
}
