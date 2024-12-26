package com.eyenet.repository;

import com.eyenet.model.entity.User;
import com.eyenet.model.entity.UserDevice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface UserDeviceRepository extends JpaRepository<UserDevice, UUID> {
    List<UserDevice> findByUserAndEnabled(User user, boolean enabled);
}
