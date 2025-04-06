package com.auth.service.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.auth.service.dto.DeviceEvent;
import com.auth.service.exceptions.KafkaPublishException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KafkaService {

    private static final String DEVICE_EVENTS_TOPIC = "thingwire.devices.events";
    private static final String DEVICE_COMMANDS_TOPIC = "thingwire.devices.commands";
    
    private final KafkaTemplate<String, DeviceEvent> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private static final Logger logger = LoggerFactory.getLogger(KafkaService.class);

    public void publishDeviceEvent(DeviceEvent event) {
        try {
            String message = objectMapper.writeValueAsString(event);
            kafkaTemplate.send(DEVICE_EVENTS_TOPIC, event.getDeviceId().toString(), event)
                .whenComplete((result, ex) -> {
                    if (ex == null) {
                        logger.info("Published device event: {}", message);
                    } else {
                        logger.error("Failed to publish device event", ex);
                    }
                });
        } catch (JsonProcessingException e) {
            logger.error("Error serializing device event", e);
            throw new KafkaPublishException("Failed to serialize device event");
        }
    }

    public void publishDeviceCommand(DeviceEvent command) {
        try {
            String message = objectMapper.writeValueAsString(command);
            kafkaTemplate.send(DEVICE_COMMANDS_TOPIC, command.getDeviceId().toString(), command)
                .whenComplete((result, ex) -> {
                    if (ex == null) {
                        logger.info("Published device command: {}", message);
                    } else {
                        logger.error("Failed to publish device command", ex);
                    }
                });
        } catch (JsonProcessingException e) {
            logger.error("Error serializing device command", e);
            throw new KafkaPublishException("Failed to serialize device command");
        }
    }
}