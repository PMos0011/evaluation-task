#!/usr/bin/env bash
set -e

schemas=(credit customer product)

PGHOST=localhost
PGPORT=5432
PGDB=$POSTGRES_DB
PGUSER=$POSTGRES_USER
PGPASSWORD_V=$POSTGRES_PASSWORD

function executeSql() {
  export PGPASSWORD=$PGPASSWORD_V
  psql -h ${PGHOST} -p ${PGPORT} -U ${PGUSER} -d ${PGDB} -tAc "$1"
}

until executeSql '\q'; do
  echo "Postgres is unavailable - sleeping"
  sleep 1
done

sleep 1
echo "Postgres is up - executing command"

for name in ${schemas[@]}; do
  sc=${name}_schema
  user=${name}_user
  pass=${name}_pass

  if [[ "$(psql -h ${PGHOST} -p ${PGPORT} -U ${PGUSER} -d ${PGDB} -tAc "SELECT 1 FROM pg_catalog.pg_namespace WHERE nspname ='$sc'")" == '1' ]]; then
    echo "Schema $sc already exists"
  else
    echo "Schema $sc does not exist"

    executeSql "
                CREATE SCHEMA IF NOT EXISTS $sc;
        "
    executeSql "
                CREATE USER $user with encrypted password '$pass';
                GRANT CONNECT ON DATABASE ${PGDB} TO $user;
                GRANT ALL PRIVILEGES ON SCHEMA $sc TO $user;
        "
  fi
done
set +e

exit 0
