package com.EmployeeService.Employee.Service.services;

import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.common.events.DepartmentCreatedEvent;

@Component
public class DepartmentEventConsumer {

    private static final Logger logger = LoggerFactory.getLogger(DepartmentEventConsumer.class);

    @Bean
    public Consumer<DepartmentCreatedEvent> departmentCreated() {
        return event -> {
            if (event == null) {
                logger.warn("Received null DepartmentCreatedEvent");
                return; // avoid infinite logging
            }

            logger.info("Received DepartmentCreatedEvent: departmentId={}, name={}",
                        event.getDepartmentId(), event.getName());

            // business logic here
            System.out.println("check:" + event.getDepartmentId() + " " + event.getName());
        };
    }
}
