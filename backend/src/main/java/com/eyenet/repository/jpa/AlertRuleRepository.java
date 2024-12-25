package com.eyenet.repository.jpa;

import com.eyenet.model.entity.Alert;
import com.eyenet.model.entity.AlertRule;
import com.eyenet.model.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AlertRuleRepository extends JpaRepository<AlertRule, UUID> {
    List<AlertRule> findByDepartmentAndEnabled(Department department, boolean enabled);
    
    List<AlertRule> findByAlertTypeAndEnabled(Alert.AlertType alertType, boolean enabled);
    
    Optional<AlertRule> findByNameAndDepartment(String name, Department department);
    
    @Query("SELECT ar FROM AlertRule ar WHERE ar.enabled = true AND " +
           "(ar.department = ?1 OR ar.department IS NULL)")
    List<AlertRule> findActiveRulesForDepartment(Department department);
    
    List<AlertRule> findByMetricNameAndEnabled(String metricName, boolean enabled);
    
    @Query("SELECT ar FROM AlertRule ar WHERE ar.deviceType = ?1 AND ar.enabled = true")
    List<AlertRule> findActiveRulesByDeviceType(String deviceType);
    
    boolean existsByNameAndDepartment(String name, Department department);
}
