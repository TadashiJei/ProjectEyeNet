package com.eyenet.repository.jpa;

import com.eyenet.model.entity.IPAssignment;
import com.eyenet.model.entity.IPRange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface IPAssignmentRepository extends JpaRepository<IPAssignment, UUID> {
    List<IPAssignment> findByIpRange(IPRange ipRange);
    
    Optional<IPAssignment> findByIpAddress(String ipAddress);
    
    Optional<IPAssignment> findByMacAddress(String macAddress);
    
    @Query("SELECT a FROM IPAssignment a WHERE a.ipRange.department.id = :departmentId")
    List<IPAssignment> findByDepartmentId(UUID departmentId);
    
    List<IPAssignment> findByStatus(IPAssignment.IPStatus status);
    
    @Query("SELECT a FROM IPAssignment a WHERE a.leaseEnd < :now AND a.status = 'ASSIGNED'")
    List<IPAssignment> findExpiredLeases(LocalDateTime now);
    
    boolean existsByIpAddress(String ipAddress);
    
    boolean existsByMacAddress(String macAddress);
    
    @Query("SELECT COUNT(a) FROM IPAssignment a WHERE a.ipRange = :range AND a.status = 'ASSIGNED'")
    long countAssignedIPs(IPRange range);
}
