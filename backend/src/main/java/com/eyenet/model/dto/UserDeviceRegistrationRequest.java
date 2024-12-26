package com.eyenet.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDeviceRegistrationRequest {
    @NotBlank(message = "Device identifier is required")
    private String deviceIdentifier;

    @NotBlank(message = "Device type is required")
    private String deviceType;

    @NotBlank(message = "Device name is required")
    @Size(max = 100)
    private String deviceName;

    private String osVersion;
    private String manufacturer;
    private String model;
    private String macAddress;
    private String ipAddress;
    private boolean trusted;
}
