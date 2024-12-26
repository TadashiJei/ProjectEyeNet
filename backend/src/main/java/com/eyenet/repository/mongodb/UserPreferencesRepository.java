package com.eyenet.repository.mongodb;

import com.eyenet.model.document.UserPreferencesDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserPreferencesRepository extends MongoRepository<UserPreferencesDocument, UUID> {
    Optional<UserPreferencesDocument> findByUserId(UUID userId);
    
    @Query("{'userId': ?0, 'preferences.theme': ?1}")
    List<UserPreferencesDocument> findByTheme(UUID userId, String theme);
    
    @Query("{'userId': ?0, 'preferences.language': ?1}")
    List<UserPreferencesDocument> findByLanguage(UUID userId, String language);
    
    @Query("{'userId': ?0, 'preferences.notifications.enabled': true}")
    List<UserPreferencesDocument> findWithNotificationsEnabled(UUID userId);
    
    boolean existsByUserId(UUID userId);
}
