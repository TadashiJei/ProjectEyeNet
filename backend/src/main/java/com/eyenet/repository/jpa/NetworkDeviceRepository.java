package com.eyenet.repository.jpa;

import com.eyenet.model.entity.NetworkDevice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface NetworkDeviceRepository extends JpaRepository<NetworkDevice, UUID> {
    Optional<NetworkDevice> findByDeviceId(String deviceId);
    
    Optional<NetworkDevice> findByIpAddress(String ipAddress);
    
    Optional<NetworkDevice> findByMacAddress(String macAddress);
    
    List<NetworkDevice> findByType(NetworkDevice.DeviceType type);
    
    @Query("SELECT d FROM NetworkDevice d WHERE d.lastSeen < :threshold")
    List<NetworkDevice> findInactiveDevices(LocalDateTime threshold);
    
    boolean existsByDeviceId(String deviceId);
    
    boolean existsByIpAddress(String ipAddress);
    
    boolean existsByMacAddress(String macAddress);
}
