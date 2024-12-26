package com.eyenet.service;

import com.eyenet.model.entity.FlowRule;
import com.eyenet.model.entity.NetworkDevice;
import com.eyenet.sdn.OpenFlowMessageHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class OpenFlowService {
    private final OpenFlowMessageHandler openFlowMessageHandler;
    private final NetworkDeviceService networkDeviceService;

    public void applyFlowRule(FlowRule flowRule) {
        NetworkDevice device = networkDeviceService.getDeviceById(flowRule.getDeviceId());
        // Implementation for applying flow rule through OpenFlow protocol
        openFlowMessageHandler.sendFlowMod(device, flowRule);
    }

    public void removeFlowRule(FlowRule flowRule) {
        NetworkDevice device = networkDeviceService.getDeviceById(flowRule.getDeviceId());
        // Implementation for removing flow rule through OpenFlow protocol
        openFlowMessageHandler.removeFlowMod(device, flowRule);
    }

    public void updateFlowRule(FlowRule flowRule) {
        NetworkDevice device = networkDeviceService.getDeviceById(flowRule.getDeviceId());
        // Implementation for updating flow rule through OpenFlow protocol
        openFlowMessageHandler.updateFlowMod(device, flowRule);
    }
}
