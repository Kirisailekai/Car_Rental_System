-- Добавление полей для социальной аутентификации
ALTER TABLE users
ADD COLUMN social_id VARCHAR(255) UNIQUE,
ADD COLUMN provider VARCHAR(50),
ADD INDEX idx_social_id (social_id);

-- Создание таблицы для хранения refresh токенов
CREATE TABLE refresh_tokens (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    token VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    INDEX idx_user_token (user_id, token)
); 