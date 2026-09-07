-- Add unique constraint on phone when no duplicate non-null phones exist
SET @dup_phones := (
    SELECT COUNT(1) FROM (
        SELECT phone FROM users
        WHERE phone IS NOT NULL AND phone <> ''
        GROUP BY phone HAVING COUNT(*) > 1
    ) duplicates
);

SET @uk_exists := (
    SELECT COUNT(1) FROM information_schema.statistics
    WHERE table_schema = DATABASE() AND table_name = 'users' AND index_name = 'uk_users_phone'
);

SET @sql := IF(
    @dup_phones = 0 AND @uk_exists = 0,
    'ALTER TABLE users ADD CONSTRAINT uk_users_phone UNIQUE (phone)',
    'SELECT 1'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Composite index for seat lock expiry cleanup
SET @idx_exists := (
    SELECT COUNT(1) FROM information_schema.statistics
    WHERE table_schema = DATABASE() AND table_name = 'seats' AND index_name = 'idx_seats_lock_expiry'
);
SET @sql := IF(
    @idx_exists = 0,
    'CREATE INDEX idx_seats_lock_expiry ON seats (status, locked_at)',
    'SELECT 1'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Composite index for pending booking cleanup
SET @idx_exists := (
    SELECT COUNT(1) FROM information_schema.statistics
    WHERE table_schema = DATABASE() AND table_name = 'bookings' AND index_name = 'idx_bookings_pending_cleanup'
);
SET @sql := IF(
    @idx_exists = 0,
    'CREATE INDEX idx_bookings_pending_cleanup ON bookings (status, booking_time)',
    'SELECT 1'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
