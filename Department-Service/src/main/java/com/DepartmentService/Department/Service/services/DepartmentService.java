package com.departmentService.department.service.services;

import com.common.events.DepartmentCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class DepartmentService {

    private static final Logger logger = LoggerFactory.getLogger(DepartmentService.class);
    private final StreamBridge streamBridge;

    public DepartmentService(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    public void createDepartment(Long departmentId, String name) {
        DepartmentCreatedEvent event = new DepartmentCreatedEvent(departmentId, name);
        logger.info("Publishing DepartmentCreatedEvent: departmentId={}, name={}", departmentId, name);

        boolean sent = streamBridge.send("departmentCreated-out-0",
                MessageBuilder.withPayload(event).build());

        if (sent) {
            logger.info("DepartmentCreatedEvent sent successfully!");
        } else {
            logger.error("Failed to send DepartmentCreatedEvent!");
        }
    }
}
