package com.eyenet.model.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "network_devices")
public class NetworkDeviceDocument {
    @Id
    private UUID id;

    @Field("name")
    private String name;

    @Field("description")
    private String description;

    @Field("device_type")
    private DeviceType deviceType;

    @Field("manufacturer")
    private String manufacturer;

    @Field("model")
    private String model;

    @Field("serial_number")
    private String serialNumber;

    @Field("firmware_version")
    private String firmwareVersion;

    @Field("ip_address")
    private String ipAddress;

    @Field("mac_address")
    private String macAddress;

    @Field("location")
    private String location;

    @Field("rack_position")
    private String rackPosition;

    @Field("ports")
    private List<PortInfo> ports;

    @Field("status")
    private DeviceStatus status;

    @Field("last_seen")
    private LocalDateTime lastSeen;

    @Field("uptime")
    private Long uptime;

    @Field("configuration")
    private Map<String, String> configuration;

    @Field("tags")
    private Set<String> tags;

    @Field("metadata")
    private Map<String, String> metadata;

    @Field("created_at")
    private LocalDateTime createdAt;

    @Field("updated_at")
    private LocalDateTime updatedAt;

    @Field("created_by")
    private UUID createdBy;

    @Field("updated_by")
    private UUID updatedBy;

    @Field("is_active")
    private boolean isActive;

    public enum DeviceType {
        ROUTER,
        SWITCH,
        FIREWALL,
        ACCESS_POINT,
        SERVER,
        STORAGE,
        LOAD_BALANCER,
        IDS_IPS,
        VPN_GATEWAY,
        OTHER
    }

    public enum DeviceStatus {
        ACTIVE,
        INACTIVE,
        MAINTENANCE,
        FAILED,
        UNKNOWN
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PortInfo {
        private String portNumber;
        private String portName;
        private String portType;
        private String speed;
        private String duplex;
        private Boolean enabled;
        private String vlan;
        private String connectedTo;
        private String status;
        private Map<String, String> properties;
    }

    public DeviceType getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(DeviceType deviceType) {
        this.deviceType = deviceType;
    }

    public List<PortInfo> getPorts() {
        return ports;
    }

    public void setPorts(List<PortInfo> ports) {
        this.ports = ports;
    }
}
