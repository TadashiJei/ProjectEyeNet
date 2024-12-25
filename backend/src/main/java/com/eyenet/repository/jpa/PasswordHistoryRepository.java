package com.eyenet.repository.jpa;

import com.eyenet.model.entity.PasswordHistory;
import com.eyenet.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface PasswordHistoryRepository extends JpaRepository<PasswordHistory, UUID> {
    List<PasswordHistory> findByUser(User user);
    
    @Query("SELECT ph FROM PasswordHistory ph WHERE ph.user = ?1 ORDER BY ph.createdAt DESC")
    List<PasswordHistory> findRecentPasswordsByUser(User user, int limit);
    
    @Query("SELECT COUNT(ph) > 0 FROM PasswordHistory ph " +
           "WHERE ph.user = ?1 AND ph.passwordHash = ?2")
    boolean isPasswordPreviouslyUsed(User user, String passwordHash);
    
    List<PasswordHistory> findByUserAndCreatedAtAfter(User user, LocalDateTime after);
    
    @Query("SELECT ph FROM PasswordHistory ph WHERE ph.user = ?1 AND " +
           "ph.changeReason = 'SECURITY_BREACH' ORDER BY ph.createdAt DESC")
    List<PasswordHistory> findSecurityBreachResets(User user);
}
