CREATE TABLE request_response_log
(
    id SERIAL PRIMARY KEY,
    service_name VARCHAR(50),
    endpoint VARCHAR(200),
    http_method VARCHAR(10),
    request_payload TEXT,
    response_payload TEXT,
    status_code INTEGER,
    error_message TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);