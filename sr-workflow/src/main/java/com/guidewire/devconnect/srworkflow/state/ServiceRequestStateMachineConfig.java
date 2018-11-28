package com.guidewire.devconnect.srworkflow.state;

import java.util.EnumSet;

import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

@Configuration
@EnableStateMachineFactory
class ServiceRequestStateMachineConfig extends EnumStateMachineConfigurerAdapter<ServiceRequestStates, ServiceRequestEvents> {

  @Override
  public void configure(StateMachineStateConfigurer<ServiceRequestStates, ServiceRequestEvents> states)
    throws Exception {
    states
      .withStates()
      .initial(ServiceRequestStates.SUBMITTED)
      .states(EnumSet.allOf(ServiceRequestStates.class));
  }

  @Override
  public void configure(StateMachineTransitionConfigurer<ServiceRequestStates, ServiceRequestEvents> transitions)
    throws Exception {
    transitions
      .withExternal()
        .source(ServiceRequestStates.SUBMITTED).target(ServiceRequestStates.IN_PROGRESS)
        .event(ServiceRequestEvents.JOB_ACCEPTED)
        .and()
      .withExternal()
        .source(ServiceRequestStates.IN_PROGRESS).target(ServiceRequestStates.FINISHED)
        .event(ServiceRequestEvents.JOB_DONE)
        .and()
      .withExternal()
        .source(ServiceRequestStates.FINISHED).target(ServiceRequestStates.CLOSED)
        .event(ServiceRequestEvents.JOB_VERIFIED);
  }
}
