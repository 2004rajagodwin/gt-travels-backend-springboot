CREATE TABLE coupons (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(50) NOT NULL,
    description VARCHAR(255) NULL,
    discount_type VARCHAR(20) NOT NULL,
    discount_value DECIMAL(10,2) NOT NULL,
    minimum_booking_amount DECIMAL(10,2) NOT NULL DEFAULT 0,
    maximum_discount DECIMAL(10,2) NULL,
    start_date DATE NOT NULL,
    expiry_date DATE NOT NULL,
    usage_limit INT NULL,
    per_user_limit INT NOT NULL DEFAULT 1,
    used_count INT NOT NULL DEFAULT 0,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT uk_coupons_code UNIQUE (code)
);

CREATE INDEX idx_coupons_active ON coupons(active);
CREATE INDEX idx_coupons_expiry_date ON coupons(expiry_date);
