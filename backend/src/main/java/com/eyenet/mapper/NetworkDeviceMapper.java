package com.eyenet.mapper;

import com.eyenet.model.document.NetworkDeviceDocument;
import com.eyenet.model.entity.NetworkDevice;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class NetworkDeviceMapper {
    
    private final ObjectMapper objectMapper;
    
    public NetworkDeviceMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
    
    public NetworkDevice mapToEntity(NetworkDeviceDocument document) {
        if (document == null) {
            return null;
        }
        
        try {
            return NetworkDevice.builder()
                    .id(document.getId())
                    .deviceId(document.getDatapathId())
                    .name(document.getName())
                    .ipAddress(document.getIpAddress())
                    .macAddress(document.getMacAddress())
                    .status(document.getStatus())
                    .lastSeen(document.getLastSeen())
                    .type(NetworkDevice.DeviceType.SWITCH)
                    .isActive(true)
                    .createdAt(document.getCreatedAt())
                    .updatedAt(document.getUpdatedAt())
                    .build();
        } catch (Exception e) {
            throw new RuntimeException("Error mapping NetworkDeviceDocument to NetworkDevice", e);
        }
    }
    
    public NetworkDeviceDocument mapToDocument(NetworkDevice entity) {
        if (entity == null) {
            return null;
        }
        
        try {
            return NetworkDeviceDocument.builder()
                    .id(entity.getId())
                    .name(entity.getName())
                    .ipAddress(entity.getIpAddress())
                    .macAddress(entity.getMacAddress())
                    .datapathId(entity.getDeviceId())
                    .status(entity.getStatus())
                    .lastSeen(entity.getLastSeen())
                    .createdAt(entity.getCreatedAt())
                    .updatedAt(entity.getUpdatedAt())
                    .build();
        } catch (Exception e) {
            throw new RuntimeException("Error mapping NetworkDevice to NetworkDeviceDocument", e);
        }
    }
}
