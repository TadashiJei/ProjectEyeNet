-- Users Table
CREATE TABLE users (
    id UUID PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    is_active BOOLEAN DEFAULT true,
    role VARCHAR(20) NOT NULL,
    password_updated_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

-- Departments Table
CREATE TABLE departments (
    id UUID PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    bandwidth_quota BIGINT,
    priority INTEGER,
    max_bandwidth INTEGER,
    daily_data_limit BIGINT,
    social_media_blocked BOOLEAN DEFAULT false,
    streaming_blocked BOOLEAN DEFAULT false,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

-- IP Ranges Table
CREATE TABLE ip_ranges (
    id UUID PRIMARY KEY,
    department_id UUID NOT NULL REFERENCES departments(id),
    start_ip VARCHAR(45) NOT NULL,
    end_ip VARCHAR(45) NOT NULL,
    subnet_mask VARCHAR(45),
    gateway VARCHAR(45),
    dns_servers TEXT,
    vlan_id INTEGER,
    is_reserved BOOLEAN DEFAULT false,
    description TEXT,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    CONSTRAINT unique_ip_range UNIQUE (start_ip, end_ip)
);

-- IP Assignments Table
CREATE TABLE ip_assignments (
    id UUID PRIMARY KEY,
    ip_range_id UUID NOT NULL REFERENCES ip_ranges(id),
    ip_address VARCHAR(45) NOT NULL UNIQUE,
    mac_address VARCHAR(17),
    hostname VARCHAR(255),
    device_type VARCHAR(50),
    lease_start TIMESTAMP,
    lease_end TIMESTAMP,
    is_static BOOLEAN DEFAULT false,
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

-- Network Devices Table
CREATE TABLE network_devices (
    id UUID PRIMARY KEY,
    device_id VARCHAR(100) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    device_type VARCHAR(50) NOT NULL,
    ip_address VARCHAR(45),
    mac_address VARCHAR(17),
    location VARCHAR(255),
    firmware_version VARCHAR(50),
    is_active BOOLEAN DEFAULT true,
    last_seen TIMESTAMP,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

-- Ports Table
CREATE TABLE ports (
    id UUID PRIMARY KEY,
    device_id UUID NOT NULL REFERENCES network_devices(id),
    port_number INTEGER NOT NULL,
    name VARCHAR(50),
    speed_mbps INTEGER,
    is_up BOOLEAN DEFAULT true,
    vlan_id INTEGER,
    port_type VARCHAR(20),
    qos_policy TEXT,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    CONSTRAINT unique_device_port UNIQUE (device_id, port_number)
);

-- QoS Policies Table
CREATE TABLE qos_policies (
    id UUID PRIMARY KEY,
    department_id UUID REFERENCES departments(id),
    name VARCHAR(100) NOT NULL,
    priority INTEGER NOT NULL,
    min_bandwidth_mbps INTEGER,
    max_bandwidth_mbps INTEGER,
    burst_size_mb INTEGER,
    dscp_marking INTEGER,
    traffic_class VARCHAR(50),
    description TEXT,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

-- Create indexes
CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_departments_name ON departments(name);
CREATE INDEX idx_ip_ranges_department ON ip_ranges(department_id);
CREATE INDEX idx_ip_assignments_range ON ip_assignments(ip_range_id);
CREATE INDEX idx_network_devices_type ON network_devices(device_type);
CREATE INDEX idx_ports_device ON ports(device_id);
CREATE INDEX idx_qos_policies_department ON qos_policies(department_id);
