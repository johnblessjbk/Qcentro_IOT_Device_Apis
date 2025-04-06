package com.auth.service.controller;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.auth.service.dto.ApiResponse;
import com.auth.service.dto.DeviceCommand;
import com.auth.service.dto.DeviceDTO;
import com.auth.service.dto.DeviceResponse;
import com.auth.service.service.DeviceService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/devices")
@RequiredArgsConstructor
public class DeviceController {
	private static final Logger logger = LoggerFactory.getLogger(DeviceController.class);

	private final DeviceService deviceService;

	@PostMapping
	public ResponseEntity<ApiResponse<DeviceResponse>> registerDevice(@Valid @RequestBody DeviceDTO deviceDTO) {
		logger.info("Registering new device: {}", deviceDTO.getName());
		DeviceResponse response = deviceService.registerDevice(deviceDTO);
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(new ApiResponse<>("success", "Device registered successfully", response));
	}

	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<DeviceResponse>> getDevice(@PathVariable UUID id) {
		logger.info("Fetching device with ID: {}", id);
		DeviceResponse response = deviceService.getDevice(id);
		return ResponseEntity.ok(new ApiResponse<>("success", "Device retrieved successfully", response));
	}

	@GetMapping
	public ResponseEntity<ApiResponse<Page<DeviceResponse>>> searchDevices(@RequestParam(required = false) String name,
			@RequestParam(defaultValue = "0")  int page,
			@RequestParam(defaultValue = "10")  int size,
			@RequestParam(defaultValue = "lastSeen") String sortBy,
			@RequestParam(defaultValue = "desc") String sortDir) {

		logger.info("Searching devices - name: {}, page: {}, size: {}, sort: {} {}", name, page, size, sortBy, sortDir);

		Sort.Direction direction = Sort.Direction.fromString(sortDir);
		Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

		Page<DeviceResponse> result = deviceService.searchDevices(name, pageable);

		return ResponseEntity.ok(new ApiResponse<>("success", "Devices retrieved successfully", result));
	}

	@PutMapping("/{id}")
	public ResponseEntity<ApiResponse<DeviceResponse>> updateDevice(@PathVariable UUID id,
			@Valid @RequestBody DeviceDTO deviceDTO) {
		logger.info("Updating device with ID: {}", id);
		DeviceResponse response = deviceService.updateDevice(id, deviceDTO);
		return ResponseEntity.ok(new ApiResponse<>("success", "Device updated successfully", response));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse<Void>> deleteDevice(@PathVariable UUID id) {
		logger.info("Deleting device with ID: {}", id);
		deviceService.deleteDevice(id);
		return ResponseEntity.ok(new ApiResponse<>("success", "Device deleted successfully", null));
	}

	@PostMapping("/{id}/send-command")
	public ResponseEntity<ApiResponse<Void>> sendCommand(@PathVariable UUID id,
			@Valid @RequestBody DeviceCommand command) {
		logger.info("Sending command to device {}: {}", id, command.getCommandType());
		deviceService.sendCommand(id, command);
		return ResponseEntity.accepted().body(new ApiResponse<>("success", "Command sent to device", null));
	}
}