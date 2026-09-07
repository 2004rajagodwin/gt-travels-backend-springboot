CREATE TABLE amenities (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(50) NOT NULL,
    name VARCHAR(120) NOT NULL,
    active TINYINT(1) NOT NULL DEFAULT 1,
    CONSTRAINT uk_amenities_code UNIQUE (code)
);

INSERT INTO amenities (code, name, active) VALUES
('WIFI', 'WiFi', 1),
('CHARGING_POINT', 'Charging Point', 1),
('WATER_BOTTLE', 'Water Bottle', 1),
('BLANKET', 'Blanket', 1),
('PILLOW', 'Pillow', 1),
('READING_LIGHT', 'Reading Light', 1),
('CCTV', 'CCTV', 1),
('EMERGENCY_EXIT', 'Emergency Exit', 1),
('LIVE_TRACKING', 'Live Tracking', 1);

ALTER TABLE buses
    ADD COLUMN bus_name VARCHAR(150) NULL,
    ADD COLUMN registration_number VARCHAR(30) NULL,
    ADD COLUMN ac_type VARCHAR(20) NULL,
    ADD COLUMN seat_configuration VARCHAR(30) NULL,
    ADD COLUMN bus_category VARCHAR(20) NULL,
    ADD COLUMN status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    ADD COLUMN description VARCHAR(1000) NULL;

UPDATE buses SET registration_number = bus_number WHERE registration_number IS NULL;
UPDATE buses SET bus_name = bus_number WHERE bus_name IS NULL;

CREATE TABLE bus_amenities (
    bus_id BIGINT NOT NULL,
    amenity_id BIGINT NOT NULL,
    PRIMARY KEY (bus_id, amenity_id),
    CONSTRAINT fk_bus_amenities_bus FOREIGN KEY (bus_id) REFERENCES buses(id) ON DELETE CASCADE,
    CONSTRAINT fk_bus_amenities_amenity FOREIGN KEY (amenity_id) REFERENCES amenities(id) ON DELETE CASCADE
);

CREATE TABLE bus_images (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    bus_id BIGINT NOT NULL,
    image_url VARCHAR(500) NOT NULL,
    primary_image TINYINT(1) NOT NULL DEFAULT 0,
    active TINYINT(1) NOT NULL DEFAULT 1,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_bus_images_bus FOREIGN KEY (bus_id) REFERENCES buses(id) ON DELETE CASCADE
);

CREATE INDEX idx_buses_status ON buses(status);
CREATE INDEX idx_buses_operator_id ON buses(operator_id);
CREATE INDEX idx_bus_images_bus_id ON bus_images(bus_id);

ALTER TABLE buses ADD CONSTRAINT uk_buses_registration_number UNIQUE (registration_number);
