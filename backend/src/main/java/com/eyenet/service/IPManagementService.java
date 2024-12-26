package com.eyenet.service;

import com.eyenet.model.entity.IPAssignment;
import com.eyenet.model.entity.IPRange;
import com.eyenet.repository.IPAssignmentRepository;
import com.eyenet.repository.IPRangeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class IPManagementService {
    private final IPRangeRepository ipRangeRepository;
    private final IPAssignmentRepository ipAssignmentRepository;

    public IPRange createIPRange(IPRange ipRange) {
        validateIPRange(ipRange);
        return ipRangeRepository.save(ipRange);
    }

    private void validateIPRange(IPRange ipRange) {
        // Check for overlapping ranges
        if (ipRangeRepository.existsByStartIpLessThanEqualAndEndIpGreaterThanEqual(
                ipRange.getEndIp(), ipRange.getStartIp())) {
            throw new IllegalArgumentException("IP range overlaps with existing range");
        }
    }

    public IPRange getIPRange(UUID id) {
        return ipRangeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("IP range not found with id: " + id));
    }

    public List<IPRange> getIPRangesByDepartment(UUID departmentId) {
        return ipRangeRepository.findByDepartmentId(departmentId);
    }

    public IPAssignment assignIP(IPAssignment ipAssignment) {
        // Check if IP is already assigned
        if (ipAssignmentRepository.existsByIpAddress(ipAssignment.getIpAddress())) {
            throw new IllegalArgumentException("IP address is already assigned");
        }

        // Check if IP is within any available range
        if (!isIPInRange(ipAssignment.getIpAddress())) {
            throw new IllegalArgumentException("IP address is not within any available range");
        }

        return ipAssignmentRepository.save(ipAssignment);
    }

    private boolean isIPInRange(String ipAddress) {
        return ipRangeRepository.existsByStartIpLessThanEqualAndEndIpGreaterThanEqual(ipAddress, ipAddress);
    }

    public void releaseIP(UUID assignmentId) {
        ipAssignmentRepository.deleteById(assignmentId);
    }

    public List<IPAssignment> getIPAssignmentsByDepartment(UUID departmentId) {
        return ipAssignmentRepository.findByDepartmentId(departmentId);
    }
}
