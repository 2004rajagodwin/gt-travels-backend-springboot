CREATE TABLE saved_passengers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    name VARCHAR(120) NOT NULL,
    age INT NOT NULL,
    gender VARCHAR(20) NOT NULL,
    mobile VARCHAR(15) NULL,
    email VARCHAR(150) NULL,
    id_type VARCHAR(30) NULL,
    id_number VARCHAR(50) NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_saved_passengers_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE INDEX idx_saved_passengers_user_id ON saved_passengers(user_id);

CREATE TABLE booking_passengers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    booking_id VARCHAR(36) NOT NULL,
    seat_id BIGINT NOT NULL,
    name VARCHAR(120) NOT NULL,
    age INT NOT NULL,
    gender VARCHAR(20) NOT NULL,
    mobile VARCHAR(15) NULL,
    email VARCHAR(150) NULL,
    id_type VARCHAR(30) NULL,
    id_number VARCHAR(50) NULL,
    CONSTRAINT fk_booking_passengers_booking FOREIGN KEY (booking_id) REFERENCES bookings(booking_id) ON DELETE CASCADE,
    CONSTRAINT fk_booking_passengers_seat FOREIGN KEY (seat_id) REFERENCES seats(id)
);

CREATE INDEX idx_booking_passengers_booking_id ON booking_passengers(booking_id);
CREATE UNIQUE INDEX uk_booking_passengers_booking_seat ON booking_passengers(booking_id, seat_id);
