ALTER TABLE users
    ADD COLUMN google_id VARCHAR(255) NULL,
    ADD COLUMN auth_provider VARCHAR(20) NOT NULL DEFAULT 'LOCAL',
    ADD COLUMN email_verified TINYINT(1) NOT NULL DEFAULT 0,
    ADD COLUMN profile_image VARCHAR(512) NULL;

UPDATE users SET email_verified = 1 WHERE enabled = 1;

CREATE TABLE email_verification_tokens (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    token_hash VARCHAR(64) NOT NULL,
    expires_at DATETIME NOT NULL,
    created_at DATETIME NOT NULL,
    used_at DATETIME NULL,
    CONSTRAINT fk_email_verification_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT uk_email_verification_hash UNIQUE (token_hash)
);

CREATE INDEX idx_email_verification_user_id ON email_verification_tokens(user_id);

SET @idx_exists := (
    SELECT COUNT(1) FROM information_schema.statistics
    WHERE table_schema = DATABASE() AND table_name = 'users' AND index_name = 'uk_users_google_id'
);
SET @sql := IF(@idx_exists = 0, 'ALTER TABLE users ADD CONSTRAINT uk_users_google_id UNIQUE (google_id)', 'SELECT 1');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
