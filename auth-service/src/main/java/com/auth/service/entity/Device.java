package com.auth.service.entity;

import jakarta.persistence.*;
import lombok.Data;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.auth.service.dto.DeviceStatus;
import com.auth.service.util.UUIDConverter;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "devices")
public class Device {

	   @Id
	    @GeneratedValue(strategy = GenerationType.AUTO)
	    @Column(columnDefinition = "BINARY(16)")
	    @Convert(converter = UUIDConverter.class)
	    private UUID id;

	    @Column(nullable = false, length = 255)
	    private String name;

	    @Enumerated(EnumType.STRING)
	    @Column(nullable = false)
	    private DeviceStatus status;

	    @Column(name = "last_seen")
	    private LocalDateTime lastSeen;

	    @JdbcTypeCode(SqlTypes.JSON)
	    @Column(columnDefinition = "json")
	    private String metadata;

	    @Version
	    private Long version;
	public Device() {
	}

	public Device(String name, DeviceStatus status, String metadata) {
		this.name = name;
		this.status = status;
		this.metadata = metadata;
		this.lastSeen = LocalDateTime.now();
	}

}
