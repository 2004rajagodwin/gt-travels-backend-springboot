ALTER TABLE users
    ADD COLUMN failed_login_attempts INT NOT NULL DEFAULT 0,
    ADD COLUMN locked_until DATETIME NULL,
    ADD COLUMN account_status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE';

UPDATE users SET account_status = 'DISABLED' WHERE enabled = 0;
UPDATE users SET account_status = 'ACTIVE' WHERE enabled = 1;
