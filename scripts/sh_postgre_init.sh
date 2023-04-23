#!/bin/sh
set -e
psql -v ON_ERROR_STOP=1 --username "postgres" --dbname "user_bot_data" <<-EOSQL
    CREATE TABLE users(
    id bigserial primary key,
    chatid bigint,
    subscribe boolean,
    admin boolean
    );
    insert into users ("chatid","admin" ) values ('441326472','true');
EOSQL
