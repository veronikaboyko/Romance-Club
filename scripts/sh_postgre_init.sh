#!/bin/sh
set -e
psql -v ON_ERROR_STOP=1 --username "postgres" --dbname "user_bot_data" <<-EOSQL
    create type state as enum('RESTART', 'START', 'STORY','SEASONSS','EPISODE','TEXT');
    CREATE TABLE users(
    id bigserial primary key,
    chatid bigint,
    subscribe boolean,
    admin boolean,
    cur_state state,
    themostlongtime bigint,
    statetimer state,
    timer timestamp
    );
    insert into users ("chatid", "admin") values (441326472, true);
    create table stats(
      id bigserial primary key,
      s_state state
    );
    create table st(
      id bigserial primary key,
      story text,
      season text,
      episode text
    );
EOSQL
