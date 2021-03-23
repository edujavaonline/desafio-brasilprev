package com.brazilprev.brazilprev.service;

import com.brazilprev.brazilprev.api.response.StateResponse;
import com.brazilprev.brazilprev.mapper.StateMapper;
import com.brazilprev.brazilprev.model.domain.State;
import com.brazilprev.brazilprev.model.repository.StateRepository;
import com.brazilprev.brazilprev.model.service.StateService;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StateServiceTest {

    @Mock
    private StateRepository stateRepository;

    @Mock
    private StateMapper stateMapper;

    @InjectMocks
    private StateService stateService;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        stateService = new StateService(stateRepository, stateMapper);
    }

    @Test
    public void shouldFindAll() {
        when(stateRepository.findAll()).thenReturn(getListStateMock());
        when(stateMapper.fromListStateToListStateResponse(getListStateMock())).thenReturn(getListStateResponseMock());

        List<StateResponse> satesResponse = stateService.findAll();

        assertEquals(getListStateResponseMock(), satesResponse);
        verify(stateRepository, only()).findAll();
    }

    private List<State> getListStateMock() {
        List<State> states = new ArrayList<>();
        State state = new State();
        state.setId(1l);
        state.setName("São Paulo");
        states.add(state);

        state = new State();
        state.setId(2l);
        state.setName("Minas Gerais");
        states.add(state);
        return states;
    }

    private List<StateResponse> getListStateResponseMock() {
        List<StateResponse> statesResponse = new ArrayList<>();
        StateResponse stateResponse = new StateResponse();
        stateResponse.setId(1l);
        stateResponse.setName("São Paulo");
        statesResponse.add(stateResponse);

        stateResponse = new StateResponse();
        stateResponse.setId(2l);
        stateResponse.setName("Minas Gerais");
        statesResponse.add(stateResponse);
        return statesResponse;
    }
}
