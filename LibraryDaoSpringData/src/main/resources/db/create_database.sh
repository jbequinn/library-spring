#!/bin/bash

HOSTNAME="develvm"
DATABASE="librarydb"
USER="libraryuser"

DB_EXISTS=$(psql --host $HOSTNAME --username postgres --no-align --tuples-only --command "SELECT COUNT(1) FROM pg_database WHERE datname='$DATABASE';")
if [[ $DB_EXISTS -gt 0 ]]; then
	echo "... database '$DATABASE' already exists. will drop first"
  DB_IN_USE=$(psql --host $HOSTNAME --username postgres --no-align --tuples-only --command "SELECT COUNT(1) FROM pg_stat_activity where datname = '$DATABASE';")
  if [[ $DB_IN_USE -gt 0 ]]; then
    echo "... database '$DATABASE' is in use, restarting postgres"
    ssh -t root@$HOSTNAME "/etc/init.d/postgresql-* restart"

    echo "... sleeping 5 seconds to let postgres be up properly"
    sleep 5s
  fi

  echo "... dropping database '$DATABASE'"
  psql --host $HOSTNAME --username postgres --command "DROP DATABASE IF EXISTS $DATABASE;"
fi

echo "... dropping role '$USER'"
psql --host $HOSTNAME --username postgres --command "DROP ROLE IF EXISTS $USER;"
echo "... creating role '$USER'"
psql --host $HOSTNAME --username postgres --command "CREATE ROLE $USER LOGIN ENCRYPTED PASSWORD 'md51d3cb0bb26e337b3cc7ecf69cbb78663' NOSUPERUSER INHERIT NOCREATEDB NOCREATEROLE NOREPLICATION;"


echo "... creating database '$DATABASE'"
createdb --host $HOSTNAME --username postgres --encoding utf8 --owner $USER --template template0 $DATABASE
