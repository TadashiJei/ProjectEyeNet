package com.eyenet.repository.jpa;

import com.eyenet.model.entity.NetworkConfiguration;
import com.eyenet.model.entity.NetworkDevice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface NetworkConfigurationRepository extends JpaRepository<NetworkConfiguration, UUID> {
    List<NetworkConfiguration> findByAppliedTo(NetworkDevice device);
    List<NetworkConfiguration> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String name, String description);
}
