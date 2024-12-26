package com.eyenet.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SecurityMetricsDTO {
    private int vulnerabilities;
    private int incidents;
    private double threatLevel;
}
