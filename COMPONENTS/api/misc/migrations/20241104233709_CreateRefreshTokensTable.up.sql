CREATE TABLE
    refresh_tokens (
        id SERIAL PRIMARY KEY,
        token TEXT NOT NULL UNIQUE,
        expiry_date TIMESTAMP NOT NULL,
        user_id INTEGER NOT NULL REFERENCES users (id),
        created_at TIMESTAMP NOT NULL DEFAULT NOW (),
        modified_at TIMESTAMP
    );

CREATE TRIGGER set_modified_at_refresh_tokens BEFORE
UPDATE ON refresh_tokens FOR EACH ROW EXECUTE FUNCTION update_modified_at ();