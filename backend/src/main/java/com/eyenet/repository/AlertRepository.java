package com.eyenet.repository;

import com.eyenet.model.entity.Alert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface AlertRepository extends JpaRepository<Alert, UUID> {
    List<Alert> findByDepartmentId(UUID departmentId);
    List<Alert> findByDepartmentIdAndStatus(UUID departmentId, Alert.AlertStatus status);
    List<Alert> findByDepartmentIdAndSeverity(UUID departmentId, Alert.Severity severity);
    List<Alert> findByDepartmentIdAndSeverityGreaterThanEqual(UUID departmentId, Alert.Severity minSeverity);
}
