package com.eyenet.repository;

import com.eyenet.model.entity.User;
import com.eyenet.model.entity.UserNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface UserNotificationRepository extends JpaRepository<UserNotification, UUID> {
    List<UserNotification> findByUserOrderByCreatedAtDesc(User user);

    @Modifying
    @Query("UPDATE UserNotification n SET n.read = true, n.readAt = :readAt WHERE n.user.id = :userId AND n.read = false")
    void markAllAsRead(UUID userId, LocalDateTime readAt);
}
