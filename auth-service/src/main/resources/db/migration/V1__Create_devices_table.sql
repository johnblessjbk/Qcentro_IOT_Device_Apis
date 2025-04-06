CREATE TABLE devices (
    id BINARY(16) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    status ENUM('ONLINE','OFFLINE','ERROR') NOT NULL,
    last_seen TIMESTAMP NULL,
    metadata JSON,
    version BIGINT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_device_status ON devices(status);
CREATE INDEX idx_device_last_seen ON devices(last_seen);