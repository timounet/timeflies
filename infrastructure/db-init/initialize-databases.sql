

CREATE ROLE martine WITH LOGIN PASSWORD 'martine' NOSUPERUSER INHERIT NOCREATEDB NOCREATEROLE NOREPLICATION;
CREATE DATABASE users_database;
GRANT ALL PRIVILEGES ON DATABASE users_database TO martine ;
GRANT ALL PRIVILEGES ON DATABASE users_database TO super;

CREATE ROLE chief WITH LOGIN PASSWORD 'chief' NOSUPERUSER INHERIT NOCREATEDB NOCREATEROLE NOREPLICATION;
CREATE DATABASE projects_database;
GRANT ALL PRIVILEGES ON DATABASE projects_database TO chief;
GRANT ALL PRIVILEGES ON DATABASE projects_database TO super;

CREATE ROLE chronos WITH LOGIN PASSWORD 'chronos' NOSUPERUSER INHERIT NOCREATEDB NOCREATEROLE NOREPLICATION;
CREATE DATABASE timers_database;
GRANT ALL PRIVILEGES ON DATABASE timers_database TO chronos;
GRANT ALL PRIVILEGES ON DATABASE timers_database TO super;

