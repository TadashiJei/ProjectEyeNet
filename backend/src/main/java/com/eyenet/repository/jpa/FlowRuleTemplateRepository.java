package com.eyenet.repository.jpa;

import com.eyenet.model.entity.FlowRuleTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FlowRuleTemplateRepository extends JpaRepository<FlowRuleTemplate, UUID> {
    List<FlowRuleTemplate> findByDepartmentIdAndActive(UUID departmentId, boolean active);
    
    List<FlowRuleTemplate> findByTypeAndActive(FlowRuleTemplate.TemplateType type, boolean active);
    
    Optional<FlowRuleTemplate> findByNameAndDepartmentId(String name, UUID departmentId);
    
    @Query("SELECT ft FROM FlowRuleTemplate ft WHERE ft.active = true AND " +
           "(ft.departmentId = ?1 OR ft.departmentId IS NULL)")
    List<FlowRuleTemplate> findActiveTemplatesForDepartment(UUID departmentId);
    
    boolean existsByNameAndDepartmentId(String name, UUID departmentId);
    
    List<FlowRuleTemplate> findByTypeAndDepartmentId(FlowRuleTemplate.TemplateType type, 
                                                  UUID departmentId);
}
