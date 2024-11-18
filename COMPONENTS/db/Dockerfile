FROM postgres:17

# Install required dependencies for pg_cron
RUN apt update && apt install -y \
    postgresql-$PG_MAJOR-cron \
    && apt clean \
    && rm -rf /var/lib/apt/lists/*

# Ensure pg_cron is available in the Postgres config and uses correct database name
RUN echo "shared_preload_libraries = 'pg_cron'" >> /usr/share/postgresql/postgresql.conf.sample
RUN echo "cron.database_name = 'speedshield'" >> /usr/share/postgresql/postgresql.conf.sample

