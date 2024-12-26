package com.eyenet.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserUsageStats {
    private long totalBytesTransferred;
    private double averageBandwidth;
    private double peakBandwidth;
    private long totalSessions;
}
