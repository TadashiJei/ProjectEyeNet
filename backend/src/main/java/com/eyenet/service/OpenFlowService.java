package com.eyenet.service;

import com.eyenet.model.document.FlowRuleDocument;
import com.eyenet.model.document.NetworkDeviceDocument;
import com.eyenet.model.entity.FlowRule;
import com.eyenet.model.entity.NetworkDevice;
import com.eyenet.mapper.FlowRuleMapper;
import com.eyenet.mapper.NetworkDeviceMapper;
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
    private final FlowRuleMapper flowRuleMapper;
    private final NetworkDeviceMapper networkDeviceMapper;

    public void applyFlowRule(FlowRule flowRule) {
        NetworkDevice device = networkDeviceService.getDeviceById(flowRule.getDeviceId());
        NetworkDeviceDocument deviceDoc = networkDeviceMapper.mapToDocument(device);
        FlowRuleDocument flowRuleDoc = flowRuleMapper.mapToDocument(flowRule);
        openFlowMessageHandler.sendFlowMod(deviceDoc, flowRuleDoc);
    }

    public void removeFlowRule(FlowRule flowRule) {
        NetworkDevice device = networkDeviceService.getDeviceById(flowRule.getDeviceId());
        NetworkDeviceDocument deviceDoc = networkDeviceMapper.mapToDocument(device);
        FlowRuleDocument flowRuleDoc = flowRuleMapper.mapToDocument(flowRule);
        openFlowMessageHandler.removeFlowMod(deviceDoc, flowRuleDoc);
    }

    public void updateFlowRule(FlowRule flowRule) {
        NetworkDevice device = networkDeviceService.getDeviceById(flowRule.getDeviceId());
        NetworkDeviceDocument deviceDoc = networkDeviceMapper.mapToDocument(device);
        FlowRuleDocument flowRuleDoc = flowRuleMapper.mapToDocument(flowRule);
        openFlowMessageHandler.updateFlowMod(deviceDoc, flowRuleDoc);
    }
}
