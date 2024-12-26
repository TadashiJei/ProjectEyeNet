package com.eyenet.mapper;

import com.eyenet.model.document.NetworkDeviceDocument;
import com.eyenet.model.dto.NetworkDeviceDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class NetworkDeviceMapper {
    
    private final ObjectMapper objectMapper;
    
    public NetworkDeviceMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
    
    public NetworkDeviceDTO toDTO(NetworkDeviceDocument document) {
        if (document == null) {
            return null;
        }
        
        return NetworkDeviceDTO.builder()
                .id(document.getId())
                .name(document.getName())
                .ipAddress(document.getIpAddress())
                .macAddress(document.getMacAddress())
                .deviceType(document.getDeviceType().toString())
                .manufacturer(document.getManufacturer())
                .model(document.getModel())
                .firmwareVersion(document.getFirmwareVersion())
                .status(document.getStatus())
                .departmentId(document.getDepartmentId())
                .location(document.getLocation())
                .lastSeen(document.getLastSeen())
                .createdAt(document.getCreatedAt())
                .updatedAt(document.getUpdatedAt())
                .createdBy(document.getCreatedBy())
                .ports(document.getPorts() != null ? document.getPorts().stream()
                        .map(this::mapPortToDTO)
                        .collect(Collectors.toSet()) : null)
                .build();
    }
    
    public NetworkDeviceDocument toDocument(NetworkDeviceDTO dto) {
        if (dto == null) {
            return null;
        }
        
        return NetworkDeviceDocument.builder()
                .id(dto.getId())
                .name(dto.getName())
                .ipAddress(dto.getIpAddress())
                .macAddress(dto.getMacAddress())
                .deviceType(NetworkDeviceDocument.DeviceType.valueOf(dto.getDeviceType()))
                .manufacturer(dto.getManufacturer())
                .model(dto.getModel())
                .firmwareVersion(dto.getFirmwareVersion())
                .status(dto.getStatus())
                .departmentId(dto.getDepartmentId())
                .location(dto.getLocation())
                .lastSeen(dto.getLastSeen())
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .createdBy(dto.getCreatedBy())
                .ports(dto.getPorts() != null ? dto.getPorts().stream()
                        .map(this::mapPortToDocument)
                        .collect(Collectors.toSet()) : null)
                .build();
    }

    private NetworkDeviceDTO.PortInfo mapPortToDTO(NetworkDeviceDocument.PortInfo portInfo) {
        return NetworkDeviceDTO.PortInfo.builder()
                .id(portInfo.getId())
                .number(portInfo.getNumber())
                .status(portInfo.getStatus())
                .build();
    }

    private NetworkDeviceDocument.PortInfo mapPortToDocument(NetworkDeviceDTO.PortInfo portInfo) {
        return NetworkDeviceDocument.PortInfo.builder()
                .id(portInfo.getId())
                .number(portInfo.getNumber())
                .status(portInfo.getStatus())
                .build();
    }
}
