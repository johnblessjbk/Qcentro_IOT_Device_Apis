package com.auth.service.service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.auth.service.dto.DeviceCommand;
import com.auth.service.dto.DeviceDTO;
import com.auth.service.dto.DeviceEvent;
import com.auth.service.dto.DeviceResponse;
import com.auth.service.dto.DeviceStatus;
import com.auth.service.entity.Device;
import com.auth.service.exceptions.DeviceNotFoundException;
import com.auth.service.repository.DeviceRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
@Service
@Transactional
@RequiredArgsConstructor
public class DeviceService {

 private final DeviceRepository deviceRepository;
 private final KafkaService kafkaService;
 public DeviceResponse registerDevice(DeviceDTO deviceDTO) {
	    Device device = new Device();
	    device.setName(deviceDTO.getName());
	    device.setStatus(deviceDTO.getStatus());
	    device.setMetadata(deviceDTO.getMetadata());
	    device.setLastSeen(LocalDateTime.now());
	    
	    Device savedDevice = deviceRepository.save(device);
	    
	    // Publish event
	    DeviceEvent event = new DeviceEvent();
	    event.setDeviceId(savedDevice.getId());
	    event.setEventType("device_registered");
	    event.setTimestamp(LocalDateTime.now());
	    event.setPayload(Map.of(
	        "name", savedDevice.getName(),
	        "status", savedDevice.getStatus().name()
	    ));
	    
	    kafkaService.publishDeviceEvent(event);
	    
	    return mapToResponse(savedDevice);
	}

 @Transactional(readOnly = true)
 public DeviceResponse getDevice(UUID id) {
     return deviceRepository.findDeviceResponseById(id)
             .orElseThrow(() -> new DeviceNotFoundException(id));
 }

 public DeviceResponse updateDevice(UUID id, DeviceDTO deviceDTO) {
     Device device = deviceRepository.findById(id)
             .orElseThrow(() -> new DeviceNotFoundException(id));
     
     device.setName(deviceDTO.getName());
     device.setStatus(deviceDTO.getStatus());
     device.setMetadata(deviceDTO.getMetadata());
     device.setLastSeen(LocalDateTime.now());
     
     return mapToResponse(device);
 }

 public void deleteDevice(UUID id) {
     deviceRepository.deleteById(id);
 }
 

 public void sendCommand(UUID deviceId, DeviceCommand command) {
     if (!deviceRepository.existsById(deviceId)) {
         throw new DeviceNotFoundException(deviceId);
     }
     
     DeviceEvent event = new DeviceEvent();
     event.setDeviceId(deviceId);
     event.setEventType("device_command");
     event.setTimestamp(LocalDateTime.now());
     event.setPayload(Map.of(
         "command", command.getCommandType(),
         "payload", command.getPayload()
     ));
     kafkaService.publishDeviceCommand(event);
 }

 public void updateDeviceStatus(UUID deviceId, DeviceStatus status) {
     deviceRepository.updateDeviceStatus(deviceId, status, LocalDateTime.now());
 }
 public Page<DeviceResponse> searchDevices(String name, Pageable pageable) {
     
     if (StringUtils.hasText(name)) {
         return deviceRepository.findByNameContainingIgnoreCase(name, pageable)
             .map(this::mapToResponse2);
     }
     
     return deviceRepository.findAll(pageable)
         .map(this::mapToResponse2);
 }

 private DeviceResponse mapToResponse2(Device device) {
     return DeviceResponse.builder()
         .id(device.getId())
         .name(device.getName())
         .status(device.getStatus())
         .lastSeen(device.getLastSeen())
         .metadata(device.getMetadata())
         .build();
 }
 private DeviceResponse mapToResponse(Device device) {
     return new DeviceResponse(
         device.getId(),
         device.getName(),
         device.getStatus(),
         device.getLastSeen(),
         device.getMetadata()
     );
 }
}
