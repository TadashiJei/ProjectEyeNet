package com.eyenet.repository;

import com.eyenet.model.entity.NetworkDevice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface NetworkDeviceRepository extends JpaRepository<NetworkDevice, UUID> {
}
