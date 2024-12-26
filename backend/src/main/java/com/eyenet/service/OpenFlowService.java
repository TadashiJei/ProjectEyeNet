package com.eyenet.service;

import com.eyenet.model.document.FlowRuleDocument;
import com.eyenet.model.document.NetworkDeviceDocument;
import com.eyenet.model.entity.NetworkDevice;
import com.eyenet.repository.NetworkDeviceRepository;
import com.eyenet.sdn.OpenFlowMessageHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OpenFlowService {
    private final OpenFlowMessageHandler openFlowMessageHandler;
    private final NetworkDeviceRepository networkDeviceRepository;

    public void applyFlowRule(FlowRuleDocument flowRule) {
        NetworkDevice device = networkDeviceRepository.findById(flowRule.getDeviceId())
                .orElseThrow(() -> new EntityNotFoundException("Device not found with id: " + flowRule.getDeviceId()));
        openFlowMessageHandler.sendFlowMod(device, flowRule);
    }

    public void removeFlowRule(FlowRuleDocument flowRule) {
        NetworkDevice device = networkDeviceRepository.findById(flowRule.getDeviceId())
                .orElseThrow(() -> new EntityNotFoundException("Device not found with id: " + flowRule.getDeviceId()));
        openFlowMessageHandler.removeFlowMod(device, flowRule);
    }

    public void updateFlowRule(FlowRuleDocument flowRule) {
        NetworkDevice device = networkDeviceRepository.findById(flowRule.getDeviceId())
                .orElseThrow(() -> new EntityNotFoundException("Device not found with id: " + flowRule.getDeviceId()));
        openFlowMessageHandler.updateFlowMod(device, flowRule);
    }

    public void queryFlowRules(UUID deviceId) {
        NetworkDevice device = networkDeviceRepository.findById(deviceId)
                .orElseThrow(() -> new EntityNotFoundException("Device not found with id: " + deviceId));
        openFlowMessageHandler.sendFlowStatsRequest(device);
    }

    public void synchronizeFlowRules(UUID deviceId) {
        NetworkDevice device = networkDeviceRepository.findById(deviceId)
                .orElseThrow(() -> new EntityNotFoundException("Device not found with id: " + deviceId));
        openFlowMessageHandler.synchronizeFlowRules(device);
    }
}
