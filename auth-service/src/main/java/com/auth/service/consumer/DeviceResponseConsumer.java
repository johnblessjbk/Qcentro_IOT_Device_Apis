package com.auth.service.consumer;

import java.util.Map;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.auth.service.dto.DeviceEvent;
import com.auth.service.dto.DeviceStatus;
import com.auth.service.service.DeviceService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class DeviceResponseConsumer {

    private final DeviceService deviceService;

    @KafkaListener(topics = "thingwire.devices.responses", groupId = "device-status-updater")
    public void consumeDeviceResponse(DeviceEvent event) {
        try {
            log.info("Received device response: {}", event);
            
            if (event.getPayload() instanceof Map) {
                Map<?, ?> payload = (Map<?, ?>) event.getPayload();
                String statusStr = (String) payload.get("status");
                DeviceStatus status = DeviceStatus.valueOf(statusStr.toUpperCase());
                
                deviceService.updateDeviceStatus(event.getDeviceId(), status);
                log.info("Updated device {} status to {}", event.getDeviceId(), status);
            }
        } catch (Exception e) {
            log.error("Error processing device response", e);
        }
    }
}