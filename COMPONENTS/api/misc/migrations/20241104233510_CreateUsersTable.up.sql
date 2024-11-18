CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username TEXT NOT NULL UNIQUE,
    email TEXT NOT NULL UNIQUE,
    password_hash TEXT NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW (),
    modified_at TIMESTAMPTZ
);
CREATE TRIGGER set_modified_at_users BEFORE
UPDATE ON users FOR EACH ROW EXECUTE FUNCTION update_modified_at ();