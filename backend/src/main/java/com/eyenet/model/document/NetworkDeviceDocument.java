package com.eyenet.model.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "network_devices")
public class NetworkDeviceDocument {
    @Id
    private UUID id;
    private String name;
    private String deviceType;
    private String ipAddress;
    private String macAddress;
    private String datapathId;
    private String location;
    private String firmwareVersion;
    private boolean isActive;
    private String status;
    private LocalDateTime lastSeen;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
