-- name: InsertRefreshToken :one
INSERT INTO refresh_tokens
(
    token,
    user_id,
    expiry_date
)
VALUES
($1, $2, $3)
RETURNING id, token, user_id, expiry_date, created_at, modified_at;