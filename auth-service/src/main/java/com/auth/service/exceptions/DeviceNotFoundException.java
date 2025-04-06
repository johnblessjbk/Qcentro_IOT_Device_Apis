package com.auth.service.exceptions;

import java.util.UUID;

public class DeviceNotFoundException extends RuntimeException {
    public DeviceNotFoundException(UUID deviceId) {
        super("Device not found with ID: " + deviceId);
    }
}