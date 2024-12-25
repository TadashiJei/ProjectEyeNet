package com.eyenet.repository.jpa;

import com.eyenet.model.entity.Department;
import com.eyenet.model.entity.FlowRule;
import com.eyenet.model.entity.NetworkDevice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface FlowRuleRepository extends JpaRepository<FlowRule, UUID> {
    List<FlowRule> findByDevice(NetworkDevice device);
    
    List<FlowRule> findByDeviceAndStatus(NetworkDevice device, FlowRule.FlowRuleStatus status);
    
    Page<FlowRule> findByDepartment(Department department, Pageable pageable);
    
    @Query("SELECT fr FROM FlowRule fr WHERE fr.device = ?1 AND fr.tableId = ?2")
    List<FlowRule> findByDeviceAndTableId(NetworkDevice device, Integer tableId);
    
    @Query("SELECT fr FROM FlowRule fr WHERE fr.status = 'ACTIVE' AND " +
           "(fr.hardTimeout IS NOT NULL AND fr.createdAt + fr.hardTimeout < ?1)")
    List<FlowRule> findExpiredRules(LocalDateTime now);
    
    @Query("SELECT COUNT(fr) FROM FlowRule fr WHERE fr.device = ?1 AND fr.status = 'ACTIVE'")
    long countActiveRulesByDevice(NetworkDevice device);
    
    List<FlowRule> findByDeviceAndPriorityGreaterThanEqual(NetworkDevice device, Integer priority);
    
    @Query("SELECT fr FROM FlowRule fr WHERE fr.status = 'ACTIVE' AND " +
           "fr.lastMatched < ?1")
    List<FlowRule> findStaleRules(LocalDateTime threshold);
    
    void deleteByDeviceAndStatus(NetworkDevice device, FlowRule.FlowRuleStatus status);
}
