CREATE TABLE users (
                       id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                       username VARCHAR(50) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       email VARCHAR(100) NOT NULL UNIQUE,
                       full_name VARCHAR(100),
                       profile_image TEXT,
                       location VARCHAR(100),
                       user_type VARCHAR(20) NOT NULL,
                       preferred_contact_method VARCHAR(50),
                       notification_preferences TEXT,
                       years_of_experience INTEGER,
                       bio TEXT,
                       languages TEXT
);

CREATE TABLE service (
                         id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                         title VARCHAR(100) NOT NULL,
                         description TEXT,
                         price NUMERIC(10, 2) NOT NULL,
                         duration INTEGER NOT NULL,
                         category VARCHAR(50),
                         created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                         provider_id BIGINT NOT NULL,
                         CONSTRAINT fk_service_provider FOREIGN KEY (provider_id) REFERENCES users(id)
);

CREATE TABLE schedule_slot (
                               id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                               start_time TIMESTAMP NOT NULL,
                               end_time TIMESTAMP NOT NULL,
                               slot_id BIGINT,
                               status VARCHAR(20) NOT NULL,
                               created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                               provider_id BIGINT NOT NULL,
                               CONSTRAINT fk_slot_provider FOREIGN KEY (provider_id) REFERENCES users(id)
);

CREATE TABLE booking (
                         id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                         created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                         client_id BIGINT NOT NULL,
                         provider_id BIGINT NOT NULL,
                         service_id BIGINT NOT NULL,
                         slot_id BIGINT NOT NULL,
                         status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
                         is_recurring BOOLEAN DEFAULT false,
                         CONSTRAINT fk_booking_client FOREIGN KEY (client_id) REFERENCES users(id),
                         CONSTRAINT fk_booking_provider FOREIGN KEY (provider_id) REFERENCES users(id),
                         CONSTRAINT fk_booking_service FOREIGN KEY (service_id) REFERENCES service(id),
                         CONSTRAINT fk_booking_slot FOREIGN KEY (slot_id) REFERENCES schedule_slot(id)
);
