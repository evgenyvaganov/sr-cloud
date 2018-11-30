package com.guidewire.devconnect.srworkflow.state;

import java.util.EnumSet;

import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

@Configuration
@EnableStateMachineFactory
public class ServiceRequestStateMachineConfig extends EnumStateMachineConfigurerAdapter<ServiceRequestStates, ServiceRequestEvents> {

  @Override
  public void configure(StateMachineStateConfigurer<ServiceRequestStates, ServiceRequestEvents> states)
    throws Exception {
    states
      .withStates()
      .initial(ServiceRequestStates.DRAFT)
      .end(ServiceRequestStates.WORK_COMPLETE)
      .end(ServiceRequestStates.CANCELED)
      .end(ServiceRequestStates.DECLINED)
      .end(ServiceRequestStates.EXPIRED)
      .states(EnumSet.allOf(ServiceRequestStates.class));
  }

  @Override
  public void configure(StateMachineTransitionConfigurer<ServiceRequestStates, ServiceRequestEvents> transitions) throws Exception {
    transitions
      .withExternal()
        .source(ServiceRequestStates.DRAFT).target(ServiceRequestStates.REQUESTED)
        .event(ServiceRequestEvents.SUBMIT_INSTRUCTION)
        .and()
      .withExternal()
        .source(ServiceRequestStates.REQUESTED).target(ServiceRequestStates.IN_PROGRESS)
        .event(ServiceRequestEvents.SPECIALIST_ACCEPTED_WORK)
        .and()
      .withExternal()
        .source(ServiceRequestStates.IN_PROGRESS).target(ServiceRequestStates.SPECIALIST_WAITING)
        .event(ServiceRequestEvents.SPECIALIST_WAITING)
        .and()
      .withExternal()
        .source(ServiceRequestStates.SPECIALIST_WAITING).target(ServiceRequestStates.IN_PROGRESS)
        .event(ServiceRequestEvents.SPECIALIST_RESUMED_WORK)
        .and()
      .withExternal()
        .source(ServiceRequestStates.IN_PROGRESS).target(ServiceRequestStates.WORK_COMPLETE)
        .event(ServiceRequestEvents.SPECIALIST_COMPLETED_WORK)
        .and()
      .withExternal()
        .source(ServiceRequestStates.REQUESTED).target(ServiceRequestStates.DECLINED)
        .event(ServiceRequestEvents.SPECIALIST_DECLINED)
        .and()
      .withExternal()
        .source(ServiceRequestStates.DRAFT).target(ServiceRequestStates.CANCELED)
        .source(ServiceRequestStates.IN_PROGRESS).target(ServiceRequestStates.CANCELED)
        .source(ServiceRequestStates.REQUESTED).target(ServiceRequestStates.CANCELED)
        .event(ServiceRequestEvents.SPECIALIST_CANCELED);
  }

  @Override
  public void configure(StateMachineConfigurationConfigurer<ServiceRequestStates, ServiceRequestEvents> config) throws Exception {
    config
      .withConfiguration()
      .autoStartup(true)
      .listener(new StateMachineListener());
  }
}
