ALTER TABLE seats
    ADD COLUMN row_number INT NULL,
    ADD COLUMN column_number INT NULL,
    ADD COLUMN deck VARCHAR(10) NULL,
    ADD COLUMN seat_layout_type VARCHAR(20) NULL DEFAULT 'SEATER',
    ADD COLUMN gender_restriction VARCHAR(20) NOT NULL DEFAULT 'GENERAL';

CREATE INDEX idx_seats_bus_id_status ON seats(bus_id, status);
