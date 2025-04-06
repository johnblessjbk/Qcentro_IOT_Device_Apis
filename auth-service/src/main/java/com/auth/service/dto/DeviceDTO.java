package com.auth.service.dto;
import com.fasterxml.jackson.annotation.JsonRawValue;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceDTO {
    @NotBlank(message = "Device name is required")
    @Size(max = 255, message = "Device name must be less than 255 characters")
    private String name;
    
    @NotNull(message = "Device status is required")
    private DeviceStatus status;
    
    @JsonRawValue
    private String metadata;
}
