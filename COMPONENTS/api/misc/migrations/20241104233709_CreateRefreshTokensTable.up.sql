CREATE EXTENSION IF NOT EXISTS "pgcrypto";
CREATE TABLE refresh_tokens (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    expiry_date TIMESTAMPTZ NOT NULL,
    user_id INTEGER NOT NULL REFERENCES users (id),
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    modified_at TIMESTAMPTZ
);
CREATE TRIGGER set_modified_at_refresh_tokens BEFORE
UPDATE ON refresh_tokens FOR EACH ROW EXECUTE FUNCTION update_modified_at();