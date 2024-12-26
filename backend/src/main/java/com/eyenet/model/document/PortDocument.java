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
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "ports")
public class PortDocument {
    @Id
    private UUID id;

    @Field("port_number")
    private int portNumber;

    @Field("name")
    private String name;

    @Field("description")
    private String description;

    @DBRef
    @Field("device")
    private NetworkDeviceDocument device;

    @Field("status")
    private String status;

    @Field("speed")
    private Long speed;

    @Field("duplex")
    private String duplex;

    @Field("is_active")
    private boolean isActive;

    @Field("last_seen")
    private LocalDateTime lastSeen;

    @Field("created_at")
    private LocalDateTime createdAt;

    @Field("updated_at")
    private LocalDateTime updatedAt;

    @Field("mac_address")
    private String macAddress;

    @Field("vlan_id")
    private Integer vlanId;

    @Field("error_count")
    private Long errorCount;

    @Field("packet_count")
    private Long packetCount;

    @Field("byte_count")
    private Long byteCount;
}
