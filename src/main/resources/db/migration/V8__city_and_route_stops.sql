CREATE TABLE cities (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(120) NOT NULL,
    state VARCHAR(120) NOT NULL,
    code VARCHAR(10) NULL,
    active TINYINT(1) NOT NULL DEFAULT 1,
    CONSTRAINT uk_cities_name_state UNIQUE (name, state)
);

CREATE INDEX idx_cities_name ON cities(name);
CREATE INDEX idx_cities_active ON cities(active);

INSERT INTO cities (name, state, code, active) VALUES
('Chennai', 'Tamil Nadu', 'MAA', 1),
('Coimbatore', 'Tamil Nadu', 'CJB', 1),
('Madurai', 'Tamil Nadu', 'IXM', 1),
('Tirunelveli', 'Tamil Nadu', 'TEN', 1),
('Trichy', 'Tamil Nadu', 'TRZ', 1),
('Salem', 'Tamil Nadu', 'SXV', 1),
('Bangalore', 'Karnataka', 'BLR', 1),
('Hyderabad', 'Telangana', 'HYD', 1),
('Kochi', 'Kerala', 'COK', 1),
('Mumbai', 'Maharashtra', 'BOM', 1),
('Pune', 'Maharashtra', 'PNQ', 1),
('Delhi', 'Delhi', 'DEL', 1);

CREATE TABLE route_stops (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    route_id BIGINT NOT NULL,
    city_name VARCHAR(120) NOT NULL,
    stop_name VARCHAR(200) NOT NULL,
    sequence_no INT NOT NULL,
    arrival_time VARCHAR(20) NULL,
    departure_time VARCHAR(20) NULL,
    distance_km INT NULL,
    active TINYINT(1) NOT NULL DEFAULT 1,
    CONSTRAINT fk_route_stops_route FOREIGN KEY (route_id) REFERENCES routes(id) ON DELETE CASCADE
);

CREATE INDEX idx_route_stops_route_id ON route_stops(route_id);
CREATE INDEX idx_route_stops_sequence ON route_stops(route_id, sequence_no);

ALTER TABLE boarding_points
    ADD COLUMN address VARCHAR(500) NULL,
    ADD COLUMN landmark VARCHAR(255) NULL,
    ADD COLUMN city VARCHAR(120) NULL,
    ADD COLUMN active TINYINT(1) NOT NULL DEFAULT 1;

ALTER TABLE dropping_points
    ADD COLUMN address VARCHAR(500) NULL,
    ADD COLUMN landmark VARCHAR(255) NULL,
    ADD COLUMN city VARCHAR(120) NULL,
    ADD COLUMN active TINYINT(1) NOT NULL DEFAULT 1;
