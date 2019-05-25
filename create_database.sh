#!/bin/bash
export PGPASSWORD=root
psql --host=localhost --username=postgres --file=tables.sql --echo-queries