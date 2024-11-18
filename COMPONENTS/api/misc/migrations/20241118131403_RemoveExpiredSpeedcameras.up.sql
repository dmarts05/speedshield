CREATE OR REPLACE FUNCTION remove_expired_speed_cameras() RETURNS VOID AS $$ BEGIN
DELETE FROM speedcameras
WHERE expiration < NOW();
END;
$$ LANGUAGE plpgsql;
-- Remove expired speed cameras every hour
SELECT cron.schedule(
        'remove_expired_speed_cameras',
        '0 * * * *',
        'SELECT remove_expired_speed_cameras();'
    );