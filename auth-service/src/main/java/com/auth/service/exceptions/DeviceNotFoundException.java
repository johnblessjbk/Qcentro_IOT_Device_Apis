package com.auth.service.exceptions;

import java.util.UUID;

import com.auth.service.util.ErrorMessages;

public class DeviceNotFoundException extends RuntimeException {
    public DeviceNotFoundException(UUID deviceId) {
        super(ErrorMessages.DEVICE_NOT_FOUND + deviceId);
    }
}