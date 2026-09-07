CREATE TABLE refunds (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    booking_id VARCHAR(36) NOT NULL,
    refund_reference VARCHAR(30) NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    reason VARCHAR(255) NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    provider VARCHAR(30) NOT NULL DEFAULT 'RAZORPAY',
    provider_refund_id VARCHAR(100) NULL,
    provider_payment_id VARCHAR(100) NULL,
    failure_reason VARCHAR(255) NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    processed_at DATETIME NULL,
    CONSTRAINT uk_refunds_reference UNIQUE (refund_reference),
    CONSTRAINT fk_refunds_booking FOREIGN KEY (booking_id) REFERENCES bookings(booking_id)
);

CREATE INDEX idx_refunds_booking_id ON refunds(booking_id);
CREATE INDEX idx_refunds_status ON refunds(status);
CREATE INDEX idx_refunds_created_at ON refunds(created_at);

CREATE TABLE audit_logs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    action_type VARCHAR(50) NOT NULL,
    entity_type VARCHAR(50) NOT NULL,
    entity_id VARCHAR(100) NULL,
    actor_id BIGINT NULL,
    actor_email VARCHAR(150) NULL,
    details TEXT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_audit_logs_action_type ON audit_logs(action_type);
CREATE INDEX idx_audit_logs_entity ON audit_logs(entity_type, entity_id);
CREATE INDEX idx_audit_logs_created_at ON audit_logs(created_at);
