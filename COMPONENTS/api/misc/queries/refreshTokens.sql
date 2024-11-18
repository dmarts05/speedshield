-- name: InsertRefreshToken :one
INSERT INTO refresh_tokens (user_id, expiry_date)
VALUES ($1, $2)
RETURNING id,
    user_id,
    expiry_date,
    created_at,
    modified_at;
-- name: DoesRefreshTokenExist :one
SELECT EXISTS(
        SELECT 1
        FROM refresh_tokens
        WHERE id = $1::uuid
            AND expiry_date > NOW()
    );
-- name: DeleteRefreshTokenById :exec
DELETE FROM refresh_tokens
WHERE id = $1::uuid;