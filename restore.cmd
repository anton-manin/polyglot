setlocal

set PGPASSWORD=root

psql -h localhost -p 5432 -U postgres -d polyglot -c "SELECT pg_terminate_backend(pg_stat_activity.pid) FROM pg_stat_activity WHERE pg_stat_activity.datname = 'polyglot' AND pid != pg_backend_pid()"
dropdb -h localhost -p 5432 -U postgres polyglot
createdb -h localhost -p 5432 -U postgres polyglot
psql -U postgres -h localhost -p 5432 polyglot < polyglot_dump.sql

endlocal

echo "Finished"