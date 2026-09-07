CREATE TABLE coupon_usages (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    coupon_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    booking_id VARCHAR(36) NULL,
    discount_amount DECIMAL(10,2) NOT NULL,
    used_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_coupon_usages_coupon FOREIGN KEY (coupon_id) REFERENCES coupons(id),
    CONSTRAINT fk_coupon_usages_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_coupon_usages_booking FOREIGN KEY (booking_id) REFERENCES bookings(booking_id)
);

CREATE INDEX idx_coupon_usages_coupon_id ON coupon_usages(coupon_id);
CREATE INDEX idx_coupon_usages_user_id ON coupon_usages(user_id);
CREATE INDEX idx_coupon_usages_booking_id ON coupon_usages(booking_id);

ALTER TABLE bookings
    ADD COLUMN coupon_id BIGINT NULL,
    ADD CONSTRAINT fk_bookings_coupon FOREIGN KEY (coupon_id) REFERENCES coupons(id);
