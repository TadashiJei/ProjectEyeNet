package com.eyenet.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BandwidthUsageDTO {
    private double totalBandwidth;
    private double averageBandwidth;
    private Double inboundBandwidth;
    private Double outboundBandwidth;
    private Map<String, Double> applicationBandwidth;
    private Map<String, Double> protocolBandwidth;
    private Long bandwidthUtilization;
}
