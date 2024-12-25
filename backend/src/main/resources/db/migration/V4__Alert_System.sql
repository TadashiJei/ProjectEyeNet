-- Alert Rules Table
CREATE TABLE alert_rules (
    id UUID PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    alert_type VARCHAR(50) NOT NULL,
    severity VARCHAR(20) NOT NULL,
    department_id UUID REFERENCES departments(id),
    device_type VARCHAR(50),
    metric_name VARCHAR(100) NOT NULL,
    condition VARCHAR(30) NOT NULL,
    threshold_value DOUBLE PRECISION NOT NULL,
    duration_minutes INTEGER,
    cooldown_minutes INTEGER,
    enabled BOOLEAN DEFAULT true,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    CONSTRAINT unique_rule_name_dept UNIQUE (name, department_id)
);

-- Alerts Table
CREATE TABLE alerts (
    id UUID PRIMARY KEY,
    alert_type VARCHAR(50) NOT NULL,
    severity VARCHAR(20) NOT NULL,
    source VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    details TEXT,
    status VARCHAR(20) NOT NULL,
    department_id UUID REFERENCES departments(id),
    device_id VARCHAR(100),
    threshold_value DOUBLE PRECISION,
    current_value DOUBLE PRECISION,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    resolved_at TIMESTAMP,
    resolved_by UUID,
    resolution_notes TEXT
);

-- Create indexes for better query performance
CREATE INDEX idx_alerts_department ON alerts(department_id);
CREATE INDEX idx_alerts_status ON alerts(status);
CREATE INDEX idx_alerts_type ON alerts(alert_type);
CREATE INDEX idx_alerts_severity ON alerts(severity);
CREATE INDEX idx_alerts_created ON alerts(created_at);
CREATE INDEX idx_alerts_device ON alerts(device_id);

CREATE INDEX idx_alert_rules_department ON alert_rules(department_id);
CREATE INDEX idx_alert_rules_type ON alert_rules(alert_type);
CREATE INDEX idx_alert_rules_metric ON alert_rules(metric_name);
CREATE INDEX idx_alert_rules_enabled ON alert_rules(enabled);

-- Add check constraints
ALTER TABLE alerts
    ADD CONSTRAINT check_valid_alert_severity
    CHECK (severity IN ('CRITICAL', 'HIGH', 'MEDIUM', 'LOW', 'INFO')),
    ADD CONSTRAINT check_valid_alert_status
    CHECK (status IN ('NEW', 'ACKNOWLEDGED', 'IN_PROGRESS', 'RESOLVED', 'CLOSED', 'FALSE_POSITIVE'));

ALTER TABLE alert_rules
    ADD CONSTRAINT check_valid_rule_severity
    CHECK (severity IN ('CRITICAL', 'HIGH', 'MEDIUM', 'LOW', 'INFO')),
    ADD CONSTRAINT check_valid_condition
    CHECK (condition IN ('GREATER_THAN', 'LESS_THAN', 'EQUALS', 'NOT_EQUALS', 
                        'GREATER_THAN_OR_EQUALS', 'LESS_THAN_OR_EQUALS')),
    ADD CONSTRAINT check_positive_duration
    CHECK (duration_minutes IS NULL OR duration_minutes > 0),
    ADD CONSTRAINT check_positive_cooldown
    CHECK (cooldown_minutes IS NULL OR cooldown_minutes > 0);

-- Insert some default alert rules
INSERT INTO alert_rules (
    id, name, description, alert_type, severity, metric_name, 
    condition, threshold_value, duration_minutes, cooldown_minutes,
    enabled, created_at, updated_at
) VALUES
(
    gen_random_uuid(),
    'High CPU Usage',
    'Alert when CPU usage exceeds 90% for 5 minutes',
    'PERFORMANCE_DEGRADATION',
    'HIGH',
    'cpu_usage',
    'GREATER_THAN',
    90.0,
    5,
    15,
    true,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
),
(
    gen_random_uuid(),
    'Critical Memory Usage',
    'Alert when memory usage exceeds 95% for 5 minutes',
    'PERFORMANCE_DEGRADATION',
    'CRITICAL',
    'memory_usage',
    'GREATER_THAN',
    95.0,
    5,
    15,
    true,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
),
(
    gen_random_uuid(),
    'High Packet Loss',
    'Alert when packet loss exceeds 5% for 3 minutes',
    'PACKET_LOSS',
    'HIGH',
    'packet_loss_percentage',
    'GREATER_THAN',
    5.0,
    3,
    10,
    true,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
);
