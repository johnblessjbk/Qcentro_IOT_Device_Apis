package com.auth.service.dto;

import com.fasterxml.jackson.annotation.JsonRawValue;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DeviceCommand {
    @NotBlank
    private String commandType;
    
    @JsonRawValue
    private String payload;
}