package com.eyenet.repository.jpa;

import com.eyenet.model.entity.Department;
import com.eyenet.model.entity.IPRange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface IPRangeRepository extends JpaRepository<IPRange, UUID> {
    List<IPRange> findByDepartment(Department department);
    
    @Query("SELECT r FROM IPRange r WHERE r.department.id = :departmentId")
    List<IPRange> findByDepartmentId(UUID departmentId);
    
    @Query("SELECT r FROM IPRange r WHERE :ip BETWEEN r.startIP AND r.endIP")
    Optional<IPRange> findByIPAddress(String ip);
    
    boolean existsByStartIPAndEndIP(String startIP, String endIP);
    
    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END FROM IPRange r " +
           "WHERE (:startIP BETWEEN r.startIP AND r.endIP) OR " +
           "(:endIP BETWEEN r.startIP AND r.endIP)")
    boolean hasOverlappingRange(String startIP, String endIP);
}
