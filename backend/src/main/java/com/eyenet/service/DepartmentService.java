package com.eyenet.service;

import com.eyenet.model.entity.Department;
import com.eyenet.repository.jpa.DepartmentRepository;
import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DepartmentService {
    private final DepartmentRepository departmentRepository;

    @Transactional
    public Department createDepartment(Department department) {
        if (departmentRepository.existsByName(department.getName())) {
            throw new IllegalArgumentException("Department with name " + department.getName() + " already exists");
        }
        return departmentRepository.save(department);
    }

    @Transactional(readOnly = true)
    public Department getDepartment(UUID id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Department not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public Department getDepartmentByName(String name) {
        return departmentRepository.findByName(name)
                .orElseThrow(() -> new EntityNotFoundException("Department not found with name: " + name));
    }

    @Transactional(readOnly = true)
    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    @Transactional
    public Department updateDepartment(UUID id, Department departmentDetails) {
        Department department = getDepartment(id);

        // Check if new name conflicts with existing department
        if (!department.getName().equals(departmentDetails.getName()) &&
            departmentRepository.existsByName(departmentDetails.getName())) {
            throw new IllegalArgumentException("Department with name " + departmentDetails.getName() + " already exists");
        }

        department.setName(departmentDetails.getName());
        department.setBandwidthQuota(departmentDetails.getBandwidthQuota());
        department.setPriority(departmentDetails.getPriority());
        department.setMaxBandwidth(departmentDetails.getMaxBandwidth());
        department.setDailyDataLimit(departmentDetails.getDailyDataLimit());
        department.setSocialMediaBlocked(departmentDetails.getSocialMediaBlocked());
        department.setStreamingBlocked(departmentDetails.getStreamingBlocked());

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
