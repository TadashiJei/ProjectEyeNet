-- Password Policies Table
CREATE TABLE password_policies (
    id UUID PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    min_length INTEGER NOT NULL,
    require_uppercase BOOLEAN DEFAULT true,
    require_lowercase BOOLEAN DEFAULT true,
    require_numbers BOOLEAN DEFAULT true,
    require_special_chars BOOLEAN DEFAULT true,
    special_chars_allowed VARCHAR(50),
    max_age_days INTEGER,
    prevent_reuse_count INTEGER,
    lockout_threshold INTEGER,
    lockout_duration_minutes INTEGER,
    is_default BOOLEAN DEFAULT false,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

-- Password History Table
CREATE TABLE password_history (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL REFERENCES users(id),
    password_hash VARCHAR(255) NOT NULL,
    salt VARCHAR(100) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    changed_by UUID,
    change_reason VARCHAR(50)
);

-- Password Resets Table
CREATE TABLE password_resets (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL REFERENCES users(id),
    token VARCHAR(255) NOT NULL UNIQUE,
    expiry TIMESTAMP NOT NULL,
    used BOOLEAN DEFAULT false,
    used_at TIMESTAMP,
    created_by UUID,
    created_at TIMESTAMP NOT NULL,
    ip_address VARCHAR(45),
    user_agent TEXT
);

-- Create indexes
CREATE INDEX idx_password_history_user ON password_history(user_id);
CREATE INDEX idx_password_resets_user ON password_resets(user_id);
CREATE INDEX idx_password_resets_token ON password_resets(token);

-- Add default password policy
INSERT INTO password_policies (
    id, 
    name, 
    min_length, 
    require_uppercase,
    require_lowercase,
    require_numbers,
    require_special_chars,
    special_chars_allowed,
    max_age_days,
    prevent_reuse_count,
    lockout_threshold,
    lockout_duration_minutes,
    is_default,
    created_at,
    updated_at
) VALUES (
    gen_random_uuid(),
    'Default Policy',
    8,
    true,
    true,
    true,
    true,
    '!@#$%^&*()_+-=[]{}|;:,.<>?',
    90,
    5,
    5,
    30,
    true,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
);
