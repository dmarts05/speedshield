CREATE TABLE user_votes (
    id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL REFERENCES users (id),
    speedcamera_id INTEGER NOT NULL REFERENCES speedcameras (id),
    vote_type TEXT NOT NULL CHECK (vote_type IN ('upvote', 'downvote')),
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW (),
    modified_at TIMESTAMPTZ,
    UNIQUE (user_id, speedcamera_id) -- Ensures that a user can vote only once for a speedcamera
);
CREATE TRIGGER set_modified_at_user_votes BEFORE
UPDATE ON user_votes FOR EACH ROW EXECUTE FUNCTION update_modified_at ();