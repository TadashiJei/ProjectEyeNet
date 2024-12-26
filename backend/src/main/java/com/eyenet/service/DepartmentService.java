package com.eyenet.service;

import com.eyenet.model.document.DepartmentDocument;
import com.eyenet.model.document.NetworkMetricsDocument;
import com.eyenet.model.entity.Department;
import com.eyenet.repository.DepartmentRepository;
import com.eyenet.repository.NetworkMetricsDocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class DepartmentService {
    private final DepartmentRepository departmentRepository;
    private final NetworkMetricsDocumentRepository networkMetricsRepository;

    public Department createDepartment(Department department) {
        return departmentRepository.save(department);
    }

    public Department updateDepartment(UUID departmentId, Department department) {
        Department existingDepartment = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new EntityNotFoundException("Department not found with id: " + departmentId));
        
        department.setId(departmentId);
        return departmentRepository.save(department);
    }

    public void deleteDepartment(UUID departmentId) {
        departmentRepository.deleteById(departmentId);
    }

    public Department getDepartmentById(UUID departmentId) {
        return departmentRepository.findById(departmentId)
                .orElseThrow(() -> new EntityNotFoundException("Department not found with id: " + departmentId));
    }

    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    public List<NetworkMetricsDocument> getDepartmentMetrics(UUID departmentId, LocalDateTime startTime, LocalDateTime endTime) {
        Department department = getDepartmentById(departmentId);
        return networkMetricsRepository.findByDepartmentIdAndTimestampBetween(departmentId.toString(), startTime, endTime);
    }

    public NetworkMetricsDocument getLatestDepartmentMetrics(UUID departmentId) {
        Department department = getDepartmentById(departmentId);
        return networkMetricsRepository.findFirstByDepartmentIdOrderByTimestampDesc(departmentId.toString())
                .orElseThrow(() -> new EntityNotFoundException("No metrics found for department: " + departmentId));
    }
}
