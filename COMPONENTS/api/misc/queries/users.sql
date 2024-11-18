-- name: InsertUser :one
INSERT INTO users (username, email, password_hash)
VALUES ($1, $2, $3)
RETURNING id,
    username,
    email,
    created_at,
    modified_at;
-- name: FindUserById :one
SELECT id,
    username,
    email,
    password_hash,
    created_at,
    modified_at
FROM users
WHERE id = $1;
-- name: FindUserByEmail :one
SELECT id,
    username,
    email,
    password_hash,
    created_at,
    modified_at
FROM users
WHERE email = $1;
-- name: DoesUserExist :one
SELECT EXISTS (
        SELECT 1
        FROM users
        WHERE username = $1
            OR email = $2
    );