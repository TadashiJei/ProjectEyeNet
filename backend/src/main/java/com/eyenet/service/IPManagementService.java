package com.eyenet.service;

import com.eyenet.model.entity.Department;
import com.eyenet.model.entity.IPAssignment;
import com.eyenet.model.entity.IPRange;
import com.eyenet.repository.jpa.IPAssignmentRepository;
import com.eyenet.repository.jpa.IPRangeRepository;
import javax.persistence.EntityNotFoundException;
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
    public IPRange createIPRange(IPRange ipRange) {
        if (ipRangeRepository.hasOverlappingRange(ipRange.getStartIP(), ipRange.getEndIP())) {
            throw new IllegalArgumentException("IP range overlaps with existing range");
        }
        return ipRangeRepository.save(ipRange);
    }

    @Transactional(readOnly = true)
    public IPRange getIPRange(UUID id) {
        return ipRangeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("IP range not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public List<IPRange> getIPRangesByDepartment(UUID departmentId) {
        return ipRangeRepository.findByDepartmentId(departmentId);
    }

    @Transactional
    public IPAssignment assignIP(UUID rangeId, IPAssignment assignment) {
        IPRange range = getIPRange(rangeId);
        
        if (ipAssignmentRepository.existsByIpAddress(assignment.getIpAddress())) {
            throw new IllegalArgumentException("IP address already assigned: " + assignment.getIpAddress());
        }
        
        if (assignment.getMacAddress() != null && 
            ipAssignmentRepository.existsByMacAddress(assignment.getMacAddress())) {
            throw new IllegalArgumentException("MAC address already assigned: " + assignment.getMacAddress());
        }
        
        assignment.setIpRange(range);
        assignment.setStatus(IPAssignment.IPStatus.ASSIGNED);
        
        return ipAssignmentRepository.save(assignment);
    }

    @Transactional
    public void releaseIP(String ipAddress) {
        IPAssignment assignment = ipAssignmentRepository.findByIpAddress(ipAddress)
                .orElseThrow(() -> new EntityNotFoundException("IP assignment not found for: " + ipAddress));
        
        assignment.setStatus(IPAssignment.IPStatus.AVAILABLE);
        assignment.setMacAddress(null);
        assignment.setHostname(null);
        assignment.setLeaseEnd(null);
        
        ipAssignmentRepository.save(assignment);
    }

    @Transactional(readOnly = true)
    public List<IPAssignment> getAssignmentsByDepartment(UUID departmentId) {
        return ipAssignmentRepository.findByDepartmentId(departmentId);
    }

    @Scheduled(fixedRate = 300000) // Run every 5 minutes
    @Transactional
    public void checkExpiredLeases() {
        List<IPAssignment> expiredLeases = ipAssignmentRepository.findExpiredLeases(LocalDateTime.now());
        for (IPAssignment lease : expiredLeases) {
            lease.setStatus(IPAssignment.IPStatus.EXPIRED);
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
