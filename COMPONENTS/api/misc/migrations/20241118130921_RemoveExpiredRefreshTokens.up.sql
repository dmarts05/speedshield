CREATE OR REPLACE FUNCTION remove_expired_refresh_tokens() RETURNS VOID AS $$ BEGIN
DELETE FROM refresh_tokens
WHERE expiry_date < NOW();
END;
$$ LANGUAGE plpgsql;
CREATE EXTENSION IF NOT EXISTS pg_cron;
-- Remove expired tokens every hour
SELECT cron.schedule(
        'remove_expired_tokens',
        '0 * * * *',
        'SELECT remove_expired_refresh_tokens();'
    );