package com.auth.service.dto;
import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Data;

@Data
public class DeviceEvent {
    private UUID deviceId;
    private String eventType;
    private LocalDateTime timestamp;
    private Object payload;
    
}