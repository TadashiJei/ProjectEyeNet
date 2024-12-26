package com.eyenet.service;

import com.eyenet.model.document.DepartmentDocument;
import com.eyenet.model.document.IPRangeDocument;
import com.eyenet.model.document.IPAssignmentDocument;
import com.eyenet.repository.mongodb.IPAssignmentRepository;
import com.eyenet.repository.mongodb.IPRangeRepository;
import com.eyenet.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class IPManagementService {
    private final IPRangeRepository ipRangeRepository;
    private final IPAssignmentRepository ipAssignmentRepository;
    private final DepartmentService departmentService;

    @Transactional
    public IPRangeDocument createIPRange(IPRangeDocument ipRange) {
        if (ipRangeRepository.hasOverlappingRange(ipRange.getStartIP(), ipRange.getEndIP())) {
            throw new IllegalArgumentException("IP range overlaps with existing range");
        }
        return ipRangeRepository.save(ipRange);
    }

    @Transactional(readOnly = true)
    public IPRangeDocument getIPRange(UUID id) {
        return ipRangeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("IP range not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public List<IPRangeDocument> getIPRangesByDepartment(UUID departmentId) {
        return ipRangeRepository.findByDepartmentId(departmentId);
    }

    @Transactional
    public IPAssignmentDocument assignIP(UUID rangeId, IPAssignmentDocument assignment) {
        IPRangeDocument range = getIPRange(rangeId);
        
        if (ipAssignmentRepository.existsByIpAddress(assignment.getIpAddress())) {
            throw new IllegalArgumentException("IP address already assigned: " + assignment.getIpAddress());
        }
        
        if (assignment.getMacAddress() != null && 
            ipAssignmentRepository.existsByMacAddress(assignment.getMacAddress())) {
            throw new IllegalArgumentException("MAC address already assigned: " + assignment.getMacAddress());
        }
        
        assignment.setIpRange(range);
        assignment.setStatus(IPAssignmentDocument.IPStatus.ASSIGNED);
        
        return ipAssignmentRepository.save(assignment);
    }

    @Transactional
    public void releaseIP(String ipAddress) {
        IPAssignmentDocument assignment = ipAssignmentRepository.findByIpAddress(ipAddress)
                .orElseThrow(() -> new EntityNotFoundException("IP assignment not found for: " + ipAddress));
        
        assignment.setStatus(IPAssignmentDocument.IPStatus.AVAILABLE);
        assignment.setMacAddress(null);
        assignment.setHostname(null);
        assignment.setLeaseEnd(null);
        
        ipAssignmentRepository.save(assignment);
    }

    @Transactional(readOnly = true)
    public List<IPAssignmentDocument> getAssignmentsByDepartment(UUID departmentId) {
        return ipAssignmentRepository.findByDepartmentId(departmentId);
    }

    @Scheduled(fixedRate = 300000) // Run every 5 minutes
    @Transactional
    public void checkExpiredLeases() {
        List<IPAssignmentDocument> expiredLeases = ipAssignmentRepository.findExpiredLeases(LocalDateTime.now());
        for (IPAssignmentDocument lease : expiredLeases) {
            lease.setStatus(IPAssignmentDocument.IPStatus.EXPIRED);
            ipAssignmentRepository.save(lease);
        }
    }

    @Transactional
    public void deleteIPRange(UUID id) {
        if (!ipRangeRepository.existsById(id)) {
            throw new EntityNotFoundException("IP range not found with id: " + id);
        }
        ipRangeRepository.deleteById(id);
    }
}
