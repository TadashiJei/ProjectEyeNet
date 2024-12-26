package com.eyenet.mapper;

import com.eyenet.model.document.NetworkDeviceDocument;
import com.eyenet.model.entity.NetworkDevice;
import org.springframework.stereotype.Component;

@Component
public class NetworkDeviceMapper {
    
    public NetworkDevice mapToEntity(NetworkDeviceDocument document) {
        if (document == null) {
            return null;
        }
        
        return NetworkDevice.builder()
                .id(document.getId())
                .name(document.getName())
                .ipAddress(document.getIpAddress())
                .macAddress(document.getMacAddress())
                .datapathId(document.getDatapathId())
                .status(document.getStatus())
                .lastSeen(document.getLastSeen())
                .capabilities(document.getCapabilities())
                .createdAt(document.getCreatedAt())
                .updatedAt(document.getUpdatedAt())
                .build();
    }
    
    public NetworkDeviceDocument mapToDocument(NetworkDevice entity) {
        if (entity == null) {
            return null;
        }
        
        return NetworkDeviceDocument.builder()
                .id(entity.getId())
                .name(entity.getName())
                .ipAddress(entity.getIpAddress())
                .macAddress(entity.getMacAddress())
                .datapathId(entity.getDatapathId())
                .status(entity.getStatus())
                .lastSeen(entity.getLastSeen())
                .capabilities(entity.getCapabilities())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
