#!/bin/sh
set -e
psql -v ON_ERROR_STOP=1 --username "postgres" --dbname "user_bot_data" <<-EOSQL
    create type state as enum('RESTART', 'START' 'STORY','SEASONSS','EPISODE','TEXT);
    CREATE TABLE users(
    id bigserial primary key,
    chatid bigint,
    subscribe boolean,
    admin boolean,
    cur_state state
    timer timestanp
    );
    insert into users ("chatid","admin" ) values ('441326472','true');
EOSQL
