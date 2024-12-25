package com.eyenet.controller;

import com.eyenet.model.entity.Department;
import com.eyenet.service.DepartmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/departments")
@RequiredArgsConstructor
public class DepartmentController {
    private final DepartmentService departmentService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Department> createDepartment(@Valid @RequestBody Department department) {
        return ResponseEntity.ok(departmentService.createDepartment(department));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<Department> getDepartment(@PathVariable UUID id) {
        return ResponseEntity.ok(departmentService.getDepartment(id));
    }

    @GetMapping("/name/{name}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<Department> getDepartmentByName(@PathVariable String name) {
        return ResponseEntity.ok(departmentService.getDepartmentByName(name));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<List<Department>> getAllDepartments() {
        return ResponseEntity.ok(departmentService.getAllDepartments());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Department> updateDepartment(
            @PathVariable UUID id,
            @Valid @RequestBody Department departmentDetails) {
        return ResponseEntity.ok(departmentService.updateDepartment(id, departmentDetails));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteDepartment(@PathVariable UUID id) {
        departmentService.deleteDepartment(id);
        return ResponseEntity.ok().build();
    }
}
