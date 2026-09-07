ALTER TABLE boarding_points
    ADD COLUMN boarding_charge DECIMAL(10,2) NOT NULL DEFAULT 0;

ALTER TABLE bookings
    ADD COLUMN pnr VARCHAR(20) NULL,
    ADD COLUMN booking_reference VARCHAR(15) NULL,
    ADD COLUMN base_fare DECIMAL(10,2) NULL,
    ADD COLUMN boarding_charges DECIMAL(10,2) NOT NULL DEFAULT 0,
    ADD COLUMN convenience_fee DECIMAL(10,2) NOT NULL DEFAULT 0,
    ADD COLUMN tax DECIMAL(10,2) NOT NULL DEFAULT 0,
    ADD COLUMN discount DECIMAL(10,2) NOT NULL DEFAULT 0,
    ADD COLUMN coupon_discount DECIMAL(10,2) NOT NULL DEFAULT 0,
    ADD COLUMN offer_discount DECIMAL(10,2) NOT NULL DEFAULT 0,
    ADD COLUMN payment_status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    ADD COLUMN boarding_point_id BIGINT NULL,
    ADD COLUMN dropping_point_id BIGINT NULL,
    ADD COLUMN created_at DATETIME NULL,
    ADD COLUMN updated_at DATETIME NULL;

UPDATE bookings SET created_at = booking_time WHERE created_at IS NULL;
UPDATE bookings SET updated_at = booking_time WHERE updated_at IS NULL;
UPDATE bookings SET base_fare = total_amount WHERE base_fare IS NULL;

ALTER TABLE bookings
    MODIFY COLUMN created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    MODIFY COLUMN updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP;

ALTER TABLE bookings
    ADD CONSTRAINT uk_bookings_pnr UNIQUE (pnr),
    ADD CONSTRAINT uk_bookings_booking_reference UNIQUE (booking_reference);

ALTER TABLE bookings
    ADD CONSTRAINT fk_bookings_boarding_point FOREIGN KEY (boarding_point_id) REFERENCES boarding_points(id),
    ADD CONSTRAINT fk_bookings_dropping_point FOREIGN KEY (dropping_point_id) REFERENCES dropping_points(id);

CREATE INDEX idx_bookings_pnr ON bookings(pnr);
CREATE INDEX idx_bookings_booking_reference ON bookings(booking_reference);
CREATE INDEX idx_bookings_passenger_id ON bookings(passenger_id);
CREATE INDEX idx_bookings_schedule_id ON bookings(schedule_id);
CREATE INDEX idx_bookings_status ON bookings(status);
CREATE INDEX idx_bookings_created_at ON bookings(created_at);
CREATE INDEX idx_bookings_payment_status ON bookings(payment_status);
