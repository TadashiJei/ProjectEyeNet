-- Flow Rules Table
CREATE TABLE flow_rules (
    id UUID PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    device_id UUID NOT NULL REFERENCES network_devices(id),
    table_id INTEGER,
    priority INTEGER NOT NULL,
    cookie BIGINT,
    match_criteria TEXT NOT NULL,
    actions TEXT NOT NULL,
    idle_timeout INTEGER,
    hard_timeout INTEGER,
    status VARCHAR(20) NOT NULL,
    department_id UUID REFERENCES departments(id),
    bytes_count BIGINT,
    packets_count BIGINT,
    last_matched TIMESTAMP,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    created_by UUID
);

-- Flow Rule Templates Table
CREATE TABLE flow_rule_templates (
    id UUID PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    template_type VARCHAR(50) NOT NULL,
    match_criteria TEXT NOT NULL,
    actions TEXT NOT NULL,
    priority INTEGER,
    idle_timeout INTEGER,
    hard_timeout INTEGER,
    department_id UUID REFERENCES departments(id),
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    created_by UUID,
    CONSTRAINT unique_template_name_dept UNIQUE (name, department_id)
);

-- Create indexes for better query performance
CREATE INDEX idx_flow_rules_device ON flow_rules(device_id);
CREATE INDEX idx_flow_rules_status ON flow_rules(status);
CREATE INDEX idx_flow_rules_department ON flow_rules(department_id);
CREATE INDEX idx_flow_rules_priority ON flow_rules(priority);
CREATE INDEX idx_flow_rules_created ON flow_rules(created_at);
CREATE INDEX idx_flow_rules_last_matched ON flow_rules(last_matched);

CREATE INDEX idx_flow_templates_type ON flow_rule_templates(template_type);
CREATE INDEX idx_flow_templates_department ON flow_rule_templates(department_id);
CREATE INDEX idx_flow_templates_active ON flow_rule_templates(is_active);

-- Add check constraints
ALTER TABLE flow_rules
    ADD CONSTRAINT check_valid_flow_status
    CHECK (status IN ('ACTIVE', 'INACTIVE', 'PENDING', 'ERROR', 'DELETED')),
    ADD CONSTRAINT check_positive_priority
    CHECK (priority >= 0),
    ADD CONSTRAINT check_valid_timeouts
    CHECK ((idle_timeout IS NULL OR idle_timeout > 0) AND 
           (hard_timeout IS NULL OR hard_timeout > 0));

ALTER TABLE flow_rule_templates
    ADD CONSTRAINT check_valid_template_type
    CHECK (template_type IN ('QOS', 'SECURITY', 'LOAD_BALANCING', 'FORWARDING', 
                           'MONITORING', 'CUSTOM')),
    ADD CONSTRAINT check_template_priority
    CHECK (priority IS NULL OR priority >= 0),
    ADD CONSTRAINT check_template_timeouts
    CHECK ((idle_timeout IS NULL OR idle_timeout > 0) AND 
           (hard_timeout IS NULL OR hard_timeout > 0));

-- Insert default QoS templates
INSERT INTO flow_rule_templates (
    id, name, description, template_type, match_criteria, actions,
    priority, idle_timeout, hard_timeout, is_active, created_at, updated_at
) VALUES
(
    gen_random_uuid(),
    'High Priority Traffic',
    'Template for high priority traffic with DSCP marking',
    'QOS',
    '{"ip_dscp": 46}',
    '{"queue": 0, "meter": 1}',
    1000,
    NULL,
    NULL,
    true,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
),
(
    gen_random_uuid(),
    'Best Effort Traffic',
    'Template for best effort traffic',
    'QOS',
    '{"ip_dscp": 0}',
    '{"queue": 1, "meter": 2}',
    100,
    NULL,
    NULL,
    true,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
),
(
    gen_random_uuid(),
    'Rate Limit Template',
    'Template for rate limiting specific flows',
    'QOS',
    '{}',
    '{"meter": 3}',
    500,
    3600,
    NULL,
    true,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
);
