package com.guidewire.devconnect.srworkflow;

import static org.assertj.core.api.Assertions.assertThat;
import com.guidewire.devconnect.srworkflow.domain.ServiceRequestEvent;
import com.guidewire.devconnect.srworkflow.domain.ServiceRequestState;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SrWorkflowApplicationTests {
  @Autowired
  @SuppressWarnings("all")
  private StateMachineFactory<ServiceRequestState, ServiceRequestEvent> _stateMachineFactory;

  @Test
  public void testInitialState() {
    StateMachine<ServiceRequestState, ServiceRequestEvent> stateMachine = _stateMachineFactory.getStateMachine();
    stateMachine.start();

    assertThat(stateMachine.getState().getId()).isEqualTo(ServiceRequestState.DRAFT);
  }

  @Test
  public void testCheckTransition() {
    StateMachine<ServiceRequestState, ServiceRequestEvent> stateMachine = _stateMachineFactory.getStateMachine();
    stateMachine.start();
    stateMachine.sendEvent(ServiceRequestEvent.SUBMIT_INSTRUCTION);

    assertThat(stateMachine.getState().getId()).isEqualTo(ServiceRequestState.REQUESTED);
  }

  @Test
  public void testCreatedStateMachineAreDifferent() {
    StateMachine<ServiceRequestState, ServiceRequestEvent> stateMachine1 = _stateMachineFactory.getStateMachine();
    StateMachine<ServiceRequestState, ServiceRequestEvent> stateMachine2 = _stateMachineFactory.getStateMachine();

    assertThat(stateMachine1).isNotSameAs(stateMachine2);
  }

  @Test
  public void testWrongTransitionDoesNotChangeState() {
    StateMachine<ServiceRequestState, ServiceRequestEvent> stateMachine = _stateMachineFactory.getStateMachine();
    stateMachine.start();

    assertThat(stateMachine.getState().getId()).isEqualTo(ServiceRequestState.DRAFT);

    stateMachine.sendEvent(ServiceRequestEvent.SPECIALIST_COMPLETED_WORK);

    assertThat(stateMachine.getState().getId()).isEqualTo(ServiceRequestState.DRAFT);
  }

  @Test
  public void testNonTerminalState() {
    StateMachine<ServiceRequestState, ServiceRequestEvent> stateMachine = _stateMachineFactory.getStateMachine();
    stateMachine.start();

    assertThat(stateMachine.isComplete()).isFalse();
  }

  @Test
  public void testTerminalState() {
    StateMachine<ServiceRequestState, ServiceRequestEvent> stateMachine = _stateMachineFactory.getStateMachine();
    stateMachine.start();

    stateMachine.sendEvent(ServiceRequestEvent.SUBMIT_INSTRUCTION);
    stateMachine.sendEvent(ServiceRequestEvent.SPECIALIST_ACCEPTED_WORK);
    stateMachine.sendEvent(ServiceRequestEvent.SPECIALIST_COMPLETED_WORK);

    assertThat(stateMachine.getState().getId()).isEqualTo(ServiceRequestState.WORK_COMPLETE);
    assertThat(stateMachine.isComplete()).isTrue();
  }
}
