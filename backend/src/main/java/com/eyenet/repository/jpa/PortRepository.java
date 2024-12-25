package com.eyenet.repository.jpa;

import com.eyenet.model.entity.NetworkDevice;
import com.eyenet.model.entity.Port;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PortRepository extends JpaRepository<Port, UUID> {
    List<Port> findByDevice(NetworkDevice device);
    
    Optional<Port> findByDeviceAndPortNumber(NetworkDevice device, Integer portNumber);
    
    List<Port> findByVlanId(Integer vlanId);
    
    List<Port> findByType(Port.PortType type);
    
    @Query("SELECT p FROM Port p WHERE p.device = :device AND p.isUp = true")
    List<Port> findActivePortsByDevice(NetworkDevice device);
    
    boolean existsByDeviceAndPortNumber(NetworkDevice device, Integer portNumber);
}
