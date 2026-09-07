-- Performance indexes for common query patterns

SET @idx_exists := (
    SELECT COUNT(1) FROM information_schema.statistics
    WHERE table_schema = DATABASE() AND table_name = 'users' AND index_name = 'idx_users_phone'
);
SET @sql := IF(@idx_exists = 0, 'CREATE INDEX idx_users_phone ON users (phone)', 'SELECT 1');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @idx_exists := (
    SELECT COUNT(1) FROM information_schema.statistics
    WHERE table_schema = DATABASE() AND table_name = 'bookings' AND index_name = 'idx_bookings_passenger_id'
);
SET @sql := IF(@idx_exists = 0, 'CREATE INDEX idx_bookings_passenger_id ON bookings (passenger_id)', 'SELECT 1');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @idx_exists := (
    SELECT COUNT(1) FROM information_schema.statistics
    WHERE table_schema = DATABASE() AND table_name = 'bookings' AND index_name = 'idx_bookings_schedule_id'
);
SET @sql := IF(@idx_exists = 0, 'CREATE INDEX idx_bookings_schedule_id ON bookings (schedule_id)', 'SELECT 1');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @idx_exists := (
    SELECT COUNT(1) FROM information_schema.statistics
    WHERE table_schema = DATABASE() AND table_name = 'bookings' AND index_name = 'idx_bookings_status'
);
SET @sql := IF(@idx_exists = 0, 'CREATE INDEX idx_bookings_status ON bookings (status)', 'SELECT 1');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @idx_exists := (
    SELECT COUNT(1) FROM information_schema.statistics
    WHERE table_schema = DATABASE() AND table_name = 'bookings' AND index_name = 'idx_bookings_booking_time'
);
SET @sql := IF(@idx_exists = 0, 'CREATE INDEX idx_bookings_booking_time ON bookings (booking_time)', 'SELECT 1');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @idx_exists := (
    SELECT COUNT(1) FROM information_schema.statistics
    WHERE table_schema = DATABASE() AND table_name = 'schedules' AND index_name = 'idx_schedules_bus_id'
);
SET @sql := IF(@idx_exists = 0, 'CREATE INDEX idx_schedules_bus_id ON schedules (bus_id)', 'SELECT 1');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @idx_exists := (
    SELECT COUNT(1) FROM information_schema.statistics
    WHERE table_schema = DATABASE() AND table_name = 'schedules' AND index_name = 'idx_schedules_route_id'
);
SET @sql := IF(@idx_exists = 0, 'CREATE INDEX idx_schedules_route_id ON schedules (route_id)', 'SELECT 1');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @idx_exists := (
    SELECT COUNT(1) FROM information_schema.statistics
    WHERE table_schema = DATABASE() AND table_name = 'schedules' AND index_name = 'idx_schedules_departure_time'
);
SET @sql := IF(@idx_exists = 0, 'CREATE INDEX idx_schedules_departure_time ON schedules (departure_time)', 'SELECT 1');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @idx_exists := (
    SELECT COUNT(1) FROM information_schema.statistics
    WHERE table_schema = DATABASE() AND table_name = 'seats' AND index_name = 'idx_seats_bus_id'
);
SET @sql := IF(@idx_exists = 0, 'CREATE INDEX idx_seats_bus_id ON seats (bus_id)', 'SELECT 1');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @idx_exists := (
    SELECT COUNT(1) FROM information_schema.statistics
    WHERE table_schema = DATABASE() AND table_name = 'seats' AND index_name = 'idx_seats_status'
);
SET @sql := IF(@idx_exists = 0, 'CREATE INDEX idx_seats_status ON seats (status)', 'SELECT 1');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @idx_exists := (
    SELECT COUNT(1) FROM information_schema.statistics
    WHERE table_schema = DATABASE() AND table_name = 'payments' AND index_name = 'idx_payments_booking_id'
);
SET @sql := IF(@idx_exists = 0, 'CREATE INDEX idx_payments_booking_id ON payments (booking_id)', 'SELECT 1');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
