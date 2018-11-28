package com.guidewire.devconnect.srworkflow;

import static org.assertj.core.api.Assertions.assertThat;
import com.guidewire.devconnect.srworkflow.state.ServiceRequestEvents;
import com.guidewire.devconnect.srworkflow.state.ServiceRequestStates;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.statemachine.StateMachine;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SrWorkflowApplicationTests {
  @Autowired
  @SuppressWarnings("all")
  private StateMachine<ServiceRequestStates, ServiceRequestEvents> _stateMachine;

  @Test
  public void testCheckTransition() {
    _stateMachine.start();
    _stateMachine.sendEvent(ServiceRequestEvents.JOB_ACCEPTED);

    assertThat(_stateMachine.getState().getId()).isEqualTo(ServiceRequestStates.IN_PROGRESS);
  }
}
