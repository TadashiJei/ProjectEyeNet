package com.eyenet.repository;

import com.eyenet.model.entity.IPAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface IPAssignmentRepository extends JpaRepository<IPAssignment, UUID> {
    List<IPAssignment> findByDepartmentId(UUID departmentId);
    boolean existsByIpAddress(String ipAddress);
}
