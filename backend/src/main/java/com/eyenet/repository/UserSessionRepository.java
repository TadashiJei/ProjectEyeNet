package com.eyenet.repository;

import com.eyenet.model.entity.User;
import com.eyenet.model.entity.UserSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface UserSessionRepository extends JpaRepository<UserSession, UUID> {
    List<UserSession> findByUserAndActiveOrderByCreatedAtDesc(User user, boolean active);

    @Modifying
    @Query("UPDATE UserSession s SET s.active = false WHERE s.user.id = :userId AND s.id != :currentSessionId")
    void terminateAllExcept(UUID userId, UUID currentSessionId);
}
