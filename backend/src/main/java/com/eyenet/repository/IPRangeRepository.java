package com.eyenet.repository;

import com.eyenet.model.entity.IPRange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface IPRangeRepository extends JpaRepository<IPRange, UUID> {
    List<IPRange> findByDepartmentId(UUID departmentId);
    boolean existsByStartIpLessThanEqualAndEndIpGreaterThanEqual(String endIp, String startIp);
}
