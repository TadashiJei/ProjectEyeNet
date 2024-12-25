-- Add foreign key constraints with cascade delete where appropriate
ALTER TABLE ip_ranges
    DROP CONSTRAINT IF EXISTS ip_ranges_department_id_fkey,
    ADD CONSTRAINT ip_ranges_department_id_fkey
    FOREIGN KEY (department_id) REFERENCES departments(id)
    ON DELETE CASCADE;

ALTER TABLE ip_assignments
    DROP CONSTRAINT IF EXISTS ip_assignments_ip_range_id_fkey,
    ADD CONSTRAINT ip_assignments_ip_range_id_fkey
    FOREIGN KEY (ip_range_id) REFERENCES ip_ranges(id)
    ON DELETE CASCADE;

ALTER TABLE ports
    DROP CONSTRAINT IF EXISTS ports_device_id_fkey,
    ADD CONSTRAINT ports_device_id_fkey
    FOREIGN KEY (device_id) REFERENCES network_devices(id)
    ON DELETE CASCADE;

ALTER TABLE qos_policies
    DROP CONSTRAINT IF EXISTS qos_policies_department_id_fkey,
    ADD CONSTRAINT qos_policies_department_id_fkey
    FOREIGN KEY (department_id) REFERENCES departments(id)
    ON DELETE SET NULL;

ALTER TABLE password_history
    DROP CONSTRAINT IF EXISTS password_history_user_id_fkey,
    ADD CONSTRAINT password_history_user_id_fkey
    FOREIGN KEY (user_id) REFERENCES users(id)
    ON DELETE CASCADE;

ALTER TABLE password_resets
    DROP CONSTRAINT IF EXISTS password_resets_user_id_fkey,
    ADD CONSTRAINT password_resets_user_id_fkey
    FOREIGN KEY (user_id) REFERENCES users(id)
    ON DELETE CASCADE;

-- Add additional indexes for performance
CREATE INDEX IF NOT EXISTS idx_ip_ranges_ip ON ip_ranges(start_ip, end_ip);
CREATE INDEX IF NOT EXISTS idx_ip_assignments_status ON ip_assignments(status);
CREATE INDEX IF NOT EXISTS idx_network_devices_active ON network_devices(is_active);
CREATE INDEX IF NOT EXISTS idx_ports_vlan ON ports(vlan_id);
CREATE INDEX IF NOT EXISTS idx_qos_policies_priority ON qos_policies(priority);
CREATE INDEX IF NOT EXISTS idx_password_history_created ON password_history(created_at);
CREATE INDEX IF NOT EXISTS idx_password_resets_expiry ON password_resets(expiry);

-- Add check constraints
ALTER TABLE departments
    ADD CONSTRAINT check_bandwidth_quota_positive
    CHECK (bandwidth_quota >= 0),
    ADD CONSTRAINT check_priority_range
    CHECK (priority BETWEEN 1 AND 10);

ALTER TABLE ip_assignments
    ADD CONSTRAINT check_valid_mac
    CHECK (mac_address ~ '^([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})$');

ALTER TABLE network_devices
    ADD CONSTRAINT check_valid_device_type
    CHECK (device_type IN ('SWITCH', 'ROUTER', 'FIREWALL', 'ACCESS_POINT', 'GATEWAY'));

ALTER TABLE ports
    ADD CONSTRAINT check_valid_port_number
    CHECK (port_number > 0),
    ADD CONSTRAINT check_valid_speed
    CHECK (speed_mbps > 0);

ALTER TABLE qos_policies
    ADD CONSTRAINT check_bandwidth_range
    CHECK (min_bandwidth_mbps <= max_bandwidth_mbps),
    ADD CONSTRAINT check_valid_dscp
    CHECK (dscp_marking BETWEEN 0 AND 63);

ALTER TABLE password_policies
    ADD CONSTRAINT check_valid_min_length
    CHECK (min_length >= 8),
    ADD CONSTRAINT check_valid_lockout
    CHECK (lockout_threshold > 0 AND lockout_duration_minutes > 0);
