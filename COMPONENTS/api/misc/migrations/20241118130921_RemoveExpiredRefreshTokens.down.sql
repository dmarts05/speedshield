SELECT cron.unschedule('remove_expired_tokens');
DROP EXTENSION IF EXISTS pg_cron;
DROP FUNCTION IF EXISTS remove_expired_refresh_tokens();