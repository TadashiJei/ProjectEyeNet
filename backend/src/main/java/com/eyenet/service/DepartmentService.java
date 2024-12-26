package com.eyenet.service;

import com.eyenet.model.document.DepartmentDocument;
import com.eyenet.repository.mongodb.DepartmentRepository;
import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DepartmentService {
    private final DepartmentRepository departmentRepository;

    @Transactional
    public DepartmentDocument createDepartment(DepartmentDocument department) {
        if (departmentRepository.existsByName(department.getName())) {
            throw new IllegalArgumentException("Department with name " + department.getName() + " already exists");
        }
        department.setId(UUID.randomUUID());
        department.setCreatedAt(LocalDateTime.now());
        department.setUpdatedAt(LocalDateTime.now());
        return departmentRepository.save(department);
    }

    @Transactional(readOnly = true)
    public DepartmentDocument getDepartment(UUID id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Department not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public DepartmentDocument getDepartmentByName(String name) {
        return departmentRepository.findByName(name)
                .orElseThrow(() -> new EntityNotFoundException("Department not found with name: " + name));
    }

    @Transactional(readOnly = true)
    public List<DepartmentDocument> getAllDepartments() {
        return departmentRepository.findAll();
    }

    @Transactional
    public DepartmentDocument updateDepartment(UUID id, DepartmentDocument departmentDetails) {
        DepartmentDocument department = getDepartment(id);

        // Check if new name conflicts with existing department
        if (!department.getName().equals(departmentDetails.getName()) &&
            departmentRepository.existsByName(departmentDetails.getName())) {
            throw new IllegalArgumentException("Department with name " + departmentDetails.getName() + " already exists");
        }

        department.setName(departmentDetails.getName());
        department.setBandwidthQuota(departmentDetails.getBandwidthQuota());
        department.setPriority(departmentDetails.getPriority());
        
        DepartmentDocument.NetworkRestrictions networkRestrictions = department.getNetworkRestrictions();
        if (networkRestrictions == null) {
            networkRestrictions = new DepartmentDocument.NetworkRestrictions();
        }
        
        DepartmentDocument.NetworkRestrictions newRestrictions = departmentDetails.getNetworkRestrictions();
        if (newRestrictions != null) {
            networkRestrictions.setMaxBandwidth(newRestrictions.getMaxBandwidth());
            networkRestrictions.setDailyDataLimit(newRestrictions.getDailyDataLimit());
            networkRestrictions.setSocialMediaBlocked(newRestrictions.getSocialMediaBlocked());
            networkRestrictions.setStreamingBlocked(newRestrictions.getStreamingBlocked());
        }
        
        department.setNetworkRestrictions(networkRestrictions);
        department.setUpdatedAt(LocalDateTime.now());

        return departmentRepository.save(department);
    }

    @Transactional
    public void deleteDepartment(UUID id) {
        if (!departmentRepository.existsById(id)) {
            throw new EntityNotFoundException("Department not found with id: " + id);
        }
        departmentRepository.deleteById(id);
    }
}
