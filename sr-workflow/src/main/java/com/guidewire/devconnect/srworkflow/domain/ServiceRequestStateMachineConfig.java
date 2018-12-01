package com.guidewire.devconnect.srworkflow.domain;

import java.util.EnumSet;

import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

@Configuration
@EnableStateMachineFactory
public class ServiceRequestStateMachineConfig extends EnumStateMachineConfigurerAdapter<ServiceRequestState, ServiceRequestEvent> {

  @Override
  public void configure(StateMachineStateConfigurer<ServiceRequestState, ServiceRequestEvent> states)
    throws Exception {
    states
      .withStates()
      .initial(ServiceRequestState.DRAFT)
      .end(ServiceRequestState.WORK_COMPLETE)
      .end(ServiceRequestState.CANCELED)
      .end(ServiceRequestState.DECLINED)
      .end(ServiceRequestState.EXPIRED)
      .states(EnumSet.allOf(ServiceRequestState.class));
  }

  @Override
  public void configure(StateMachineTransitionConfigurer<ServiceRequestState, ServiceRequestEvent> transitions) throws Exception {
    transitions
      .withExternal()
        .source(ServiceRequestState.DRAFT).target(ServiceRequestState.REQUESTED)
        .event(ServiceRequestEvent.SUBMIT_INSTRUCTION)
        .and()
      .withExternal()
        .source(ServiceRequestState.REQUESTED).target(ServiceRequestState.IN_PROGRESS)
        .event(ServiceRequestEvent.SPECIALIST_ACCEPTED_WORK)
        .and()
      .withExternal()
        .source(ServiceRequestState.IN_PROGRESS).target(ServiceRequestState.SPECIALIST_WAITING)
        .event(ServiceRequestEvent.SPECIALIST_WAITING)
        .and()
      .withExternal()
        .source(ServiceRequestState.SPECIALIST_WAITING).target(ServiceRequestState.IN_PROGRESS)
        .event(ServiceRequestEvent.SPECIALIST_RESUMED_WORK)
        .and()
      .withExternal()
        .source(ServiceRequestState.IN_PROGRESS).target(ServiceRequestState.WORK_COMPLETE)
        .event(ServiceRequestEvent.SPECIALIST_COMPLETED_WORK)
        .and()
      .withExternal()
        .source(ServiceRequestState.REQUESTED).target(ServiceRequestState.DECLINED)
        .event(ServiceRequestEvent.SPECIALIST_DECLINED)
        .and()
      .withExternal()
        .source(ServiceRequestState.DRAFT).target(ServiceRequestState.CANCELED)
        .source(ServiceRequestState.IN_PROGRESS).target(ServiceRequestState.CANCELED)
        .source(ServiceRequestState.REQUESTED).target(ServiceRequestState.CANCELED)
        .event(ServiceRequestEvent.SPECIALIST_CANCELED);
  }

  @Override
  public void configure(StateMachineConfigurationConfigurer<ServiceRequestState, ServiceRequestEvent> config) throws Exception {
    config
      .withConfiguration()
      .autoStartup(true)
      .listener(new StateMachineListener());
  }
}
