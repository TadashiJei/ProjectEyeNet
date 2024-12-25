package com.eyenet.repository.jpa;

import com.eyenet.model.entity.Alert;
import com.eyenet.model.entity.Department;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface AlertRepository extends JpaRepository<Alert, UUID> {
    Page<Alert> findByDepartment(Department department, Pageable pageable);
    
    List<Alert> findByStatusIn(List<Alert.AlertStatus> statuses);
    
    List<Alert> findByDeviceIdAndStatusNot(String deviceId, Alert.AlertStatus status);
    
    @Query("SELECT a FROM Alert a WHERE a.status != 'RESOLVED' AND a.severity IN ('CRITICAL', 'HIGH')")
    List<Alert> findActiveHighPriorityAlerts();
    
    List<Alert> findByTypeAndCreatedAtAfter(Alert.AlertType type, LocalDateTime after);
    
    @Query("SELECT COUNT(a) FROM Alert a WHERE a.department = ?1 AND a.status != 'RESOLVED' " +
           "AND a.createdAt >= ?2")
    long countActiveAlertsByDepartment(Department department, LocalDateTime since);
    
    @Query("SELECT a FROM Alert a WHERE a.status NOT IN ('RESOLVED', 'CLOSED') " +
           "AND a.severity = ?1 ORDER BY a.createdAt DESC")
    List<Alert> findUnresolvedAlertsBySeverity(Alert.Severity severity);
    
    List<Alert> findBySourceAndStatusNotIn(String source, List<Alert.AlertStatus> statuses);
}
