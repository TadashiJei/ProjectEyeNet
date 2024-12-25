package com.eyenet.repository.jpa;

import com.eyenet.model.entity.PasswordReset;
import com.eyenet.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PasswordResetRepository extends JpaRepository<PasswordReset, UUID> {
    Optional<PasswordReset> findByToken(String token);
    
    List<PasswordReset> findByUser(User user);
    
    @Query("SELECT pr FROM PasswordReset pr WHERE pr.user = ?1 AND pr.used = false AND pr.expiry > ?2")
    List<PasswordReset> findValidTokensByUser(User user, LocalDateTime now);
    
    @Query("SELECT pr FROM PasswordReset pr WHERE pr.expiry < ?1 AND pr.used = false")
    List<PasswordReset> findExpiredUnusedTokens(LocalDateTime now);
    
    boolean existsByTokenAndUsedFalseAndExpiryAfter(String token, LocalDateTime now);
    
    @Query("SELECT COUNT(pr) FROM PasswordReset pr WHERE pr.user = ?1 AND " +
           "pr.createdAt > ?2 AND pr.used = false")
    long countRecentResetRequests(User user, LocalDateTime since);
}
