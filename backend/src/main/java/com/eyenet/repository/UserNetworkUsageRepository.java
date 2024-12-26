package com.eyenet.repository;

import com.eyenet.model.entity.User;
import com.eyenet.model.entity.UserNetworkUsage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserNetworkUsageRepository extends JpaRepository<UserNetworkUsage, UUID> {
    @Query("SELECT u FROM UserNetworkUsage u WHERE u.user = :user ORDER BY u.timestamp DESC LIMIT 1")
    Optional<UserNetworkUsage> findLatestByUser(User user);

    List<UserNetworkUsage> findByDepartmentIdAndTimestampBetween(UUID departmentId, LocalDateTime start, LocalDateTime end);
}
