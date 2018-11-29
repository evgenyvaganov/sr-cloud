package com.guidewire.devconnect.srworkflow;

import static org.assertj.core.api.Assertions.assertThat;
import com.guidewire.devconnect.srworkflow.state.ServiceRequestEvents;
import com.guidewire.devconnect.srworkflow.state.ServiceRequestStates;
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
  private StateMachineFactory<ServiceRequestStates, ServiceRequestEvents> _stateMachineFactory;

  @Test
  public void testInitialState() {
    StateMachine<ServiceRequestStates, ServiceRequestEvents> stateMachine = _stateMachineFactory.getStateMachine();
    stateMachine.start();

    assertThat(stateMachine.getState().getId()).isEqualTo(ServiceRequestStates.DRAFT);
  }

  @Test
  public void testCheckTransition() {
    StateMachine<ServiceRequestStates, ServiceRequestEvents> stateMachine = _stateMachineFactory.getStateMachine();
    stateMachine.start();
    stateMachine.sendEvent(ServiceRequestEvents.SUBMIT_INSTRUCTION);

    assertThat(stateMachine.getState().getId()).isEqualTo(ServiceRequestStates.REQUESTED);
  }

  @Test
  public void testCreatedStateMachineAreDifferent() {
    StateMachine<ServiceRequestStates, ServiceRequestEvents> stateMachine1 = _stateMachineFactory.getStateMachine();
    StateMachine<ServiceRequestStates, ServiceRequestEvents> stateMachine2 = _stateMachineFactory.getStateMachine();

    assertThat(stateMachine1).isNotSameAs(stateMachine2);
  }

  @Test
  public void testWrongTransitionDoesNotChangeState() {
    StateMachine<ServiceRequestStates, ServiceRequestEvents> stateMachine = _stateMachineFactory.getStateMachine();
    stateMachine.start();

    assertThat(stateMachine.getState().getId()).isEqualTo(ServiceRequestStates.DRAFT);

    stateMachine.sendEvent(ServiceRequestEvents.SPECIALIST_COMPLETED_WORK);

    assertThat(stateMachine.getState().getId()).isEqualTo(ServiceRequestStates.DRAFT);
  }

  @Test
  public void testNonTerminalState() {
    StateMachine<ServiceRequestStates, ServiceRequestEvents> stateMachine = _stateMachineFactory.getStateMachine();
    stateMachine.start();

    assertThat(stateMachine.isComplete()).isFalse();
  }

  @Test
  public void testTerminalState() {
    StateMachine<ServiceRequestStates, ServiceRequestEvents> stateMachine = _stateMachineFactory.getStateMachine();
    stateMachine.start();

    stateMachine.sendEvent(ServiceRequestEvents.SUBMIT_INSTRUCTION);
    stateMachine.sendEvent(ServiceRequestEvents.SPECIALIST_ACCEPTED_WORK);
    stateMachine.sendEvent(ServiceRequestEvents.SPECIALIST_COMPLETED_WORK);

    assertThat(stateMachine.getState().getId()).isEqualTo(ServiceRequestStates.WORK_COMPLETE);
    assertThat(stateMachine.isComplete()).isTrue();
  }
}
