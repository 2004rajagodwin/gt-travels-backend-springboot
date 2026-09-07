CREATE TABLE promotional_offers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(120) NOT NULL,
    description VARCHAR(255) NULL,
    discount_type VARCHAR(20) NOT NULL,
    discount_value DECIMAL(10,2) NOT NULL,
    maximum_discount DECIMAL(10,2) NULL,
    minimum_booking_amount DECIMAL(10,2) NOT NULL DEFAULT 0,
    start_date DATE NOT NULL,
    expiry_date DATE NOT NULL,
    scope_type VARCHAR(20) NOT NULL DEFAULT 'GLOBAL',
    scope_id BIGINT NULL,
    first_booking_only BOOLEAN NOT NULL DEFAULT FALSE,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE INDEX idx_promotional_offers_active ON promotional_offers(active);
CREATE INDEX idx_promotional_offers_scope ON promotional_offers(scope_type, scope_id);

CREATE TABLE cancellation_policies (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(120) NOT NULL,
    scope_type VARCHAR(20) NOT NULL DEFAULT 'GLOBAL',
    scope_id BIGINT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE INDEX idx_cancellation_policies_scope ON cancellation_policies(scope_type, scope_id);

CREATE TABLE cancellation_policy_rules (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    policy_id BIGINT NOT NULL,
    min_hours_before_journey INT NOT NULL,
    refund_percentage DECIMAL(5,2) NOT NULL,
    CONSTRAINT fk_cancellation_rules_policy FOREIGN KEY (policy_id) REFERENCES cancellation_policies(id) ON DELETE CASCADE
);

CREATE INDEX idx_cancellation_rules_policy ON cancellation_policy_rules(policy_id);

ALTER TABLE bookings
    ADD COLUMN offer_id BIGINT NULL,
    ADD COLUMN cancelled_at DATETIME NULL,
    ADD COLUMN cancellation_charge DECIMAL(10,2) NULL,
    ADD COLUMN refund_amount DECIMAL(10,2) NULL,
    ADD CONSTRAINT fk_bookings_offer FOREIGN KEY (offer_id) REFERENCES promotional_offers(id);
