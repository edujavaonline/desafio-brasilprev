ALTER TABLE client_register ADD client_id VARCHAR(36) NOT NULL AFTER id;
ALTER TABLE client_register ADD CONSTRAINT uk_client_id UNIQUE (client_id);