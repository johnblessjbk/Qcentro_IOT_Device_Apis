package com.auth.service.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.auth.service.dto.DeviceResponse;
import com.auth.service.dto.DeviceStatus;
import com.auth.service.entity.Device;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DeviceRepository extends JpaRepository<Device, UUID> {

    @Modifying
    @Query("UPDATE Device d SET d.status = :status, d.lastSeen = :timestamp WHERE d.id = :id")
    void updateDeviceStatus(@Param("id") UUID id, 
                          @Param("status") DeviceStatus status,
                          @Param("timestamp") LocalDateTime timestamp);

    @Query("SELECT new com.auth.service.dto.DeviceResponse(d.id, d.name, d.status, d.lastSeen, d.metadata) FROM Device d WHERE d.id = :id")
    Optional<DeviceResponse> findDeviceResponseById(@Param("id") UUID id);
    
    
    @Query("SELECT d FROM Device d WHERE " +
            "(:name IS NULL OR LOWER(d.name) LIKE LOWER(concat('%', :name,'%')))")
     Page<Device> findByNameContainingIgnoreCase(
         @Param("name") String name, 
         Pageable pageable);
     
     Page<Device> findByNameContaining(String name, Pageable pageable);
     
     @Query("SELECT COUNT(d) > 0 FROM Device d WHERE LOWER(d.name) = LOWER(:name)")
     boolean existsByNameIgnoreCase(@Param("name") String name);
}