package com.eyenet.repository.jpa;

import com.eyenet.model.entity.Department;
import com.eyenet.model.entity.QoSPolicy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface QoSPolicyRepository extends JpaRepository<QoSPolicy, UUID> {
    List<QoSPolicy> findByDepartment(Department department);
    
    Optional<QoSPolicy> findByName(String name);
    
    List<QoSPolicy> findByTrafficClass(QoSPolicy.TrafficClass trafficClass);
    
    List<QoSPolicy> findByPriorityGreaterThanEqual(Integer priority);
    
    boolean existsByNameAndDepartment(String name, Department department);
}
