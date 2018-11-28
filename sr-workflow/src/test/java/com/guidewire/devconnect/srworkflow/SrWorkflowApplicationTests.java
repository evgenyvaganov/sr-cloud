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
  public void testCheckTransition() {
    StateMachine<ServiceRequestStates, ServiceRequestEvents> stateMachine = _stateMachineFactory.getStateMachine();
    stateMachine.start();
    stateMachine.sendEvent(ServiceRequestEvents.JOB_ACCEPTED);

    assertThat(stateMachine.getState().getId()).isEqualTo(ServiceRequestStates.IN_PROGRESS);
  }

  @Test
  public void testCreatedStateMachineAreDifferent() {
    StateMachine<ServiceRequestStates, ServiceRequestEvents> stateMachine1 = _stateMachineFactory.getStateMachine();
    StateMachine<ServiceRequestStates, ServiceRequestEvents> stateMachine2 = _stateMachineFactory.getStateMachine();

    assertThat(stateMachine1).isNotSameAs(stateMachine2);
  }
}
