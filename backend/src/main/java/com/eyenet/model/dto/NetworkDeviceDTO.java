package com.eyenet.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NetworkDeviceDTO {
    private UUID id;
    private String name;
    private String ipAddress;
    private String macAddress;
    private String deviceType;
    private String manufacturer;
    private String model;
    private String firmwareVersion;
    private String status;
    private UUID departmentId;
    private String location;
    private LocalDateTime lastSeen;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UUID createdBy;
    private Set<PortInfo> ports;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PortInfo {
        private UUID id;
        private Integer number;
        private String status;
    }
}
