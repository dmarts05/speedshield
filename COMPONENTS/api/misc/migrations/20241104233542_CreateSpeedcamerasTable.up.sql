CREATE TABLE
    speedcameras (
        id SERIAL PRIMARY KEY,
        latitude DECIMAL NOT NULL,
        longitude DECIMAL NOT NULL,
        type INTEGER NOT NULL REFERENCES speedcameras_types (id),
        speed_limit INTEGER,
        direction_type INTEGER,
        direction INTEGER,
        location_description TEXT,
        source INTEGER NOT NULL REFERENCES speedcameras_sources (id),
        expiration TIMESTAMP,
        created_by INTEGER REFERENCES users (id),
        created_at TIMESTAMP NOT NULL DEFAULT NOW (),
        modified_at TIMESTAMP
    );

CREATE TRIGGER set_modified_at_speedcameras BEFORE
UPDATE ON speedcameras FOR EACH ROW EXECUTE FUNCTION update_modified_at ();