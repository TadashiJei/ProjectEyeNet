package com.eyenet.repository.jpa;

import com.eyenet.model.entity.Department;
import com.eyenet.model.entity.FlowRuleTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FlowRuleTemplateRepository extends JpaRepository<FlowRuleTemplate, UUID> {
    List<FlowRuleTemplate> findByDepartmentAndActive(Department department, boolean active);
    
    List<FlowRuleTemplate> findByTypeAndActive(FlowRuleTemplate.TemplateType type, boolean active);
    
    Optional<FlowRuleTemplate> findByNameAndDepartment(String name, Department department);
    
    @Query("SELECT ft FROM FlowRuleTemplate ft WHERE ft.active = true AND " +
           "(ft.department = ?1 OR ft.department IS NULL)")
    List<FlowRuleTemplate> findActiveTemplatesForDepartment(Department department);
    
    boolean existsByNameAndDepartment(String name, Department department);
    
    List<FlowRuleTemplate> findByTypeAndDepartment(FlowRuleTemplate.TemplateType type, 
                                                  Department department);
}
