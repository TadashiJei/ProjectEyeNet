package com.eyenet.repository;

import com.eyenet.model.entity.User;
import com.eyenet.model.entity.UserActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface UserActivityRepository extends JpaRepository<UserActivity, UUID> {
    List<UserActivity> findByUserOrderByCreatedAtDesc(User user);
}
