# Infrastructure
What’s this infra?
Any microservice system is going to rely on a set of technical services. In our context, we are going to use PostgreSQL as the database, Prometheus as the monitoring tool, and Kafka as the event/message bus. This infrastructure starts all these services, so you don’t have to worry about them.

## Start dev infra
To warm up your Docker image repository, navigate to the `infrastructure` directory. Here, you will find a `docker-compose.yaml`/`docker-compose-linux.yaml` files which defines all the needed Docker images. Notice that there is a `db-init` directory with a `initialize-databases.sql` script which sets up our databases and a `monitoring` directory (all that will be explained later).
In a short way
`````shell script
# On non Linux environment run
$ docker-compose -f docker-compose.yaml up -d
# On linux run
$ docker-compose -f docker-compose-linux.yaml up -d
`````
This will download all the Docker images and start the containers.

### In case of issue
If you have an issue creating the roles for the database with the `initialize-databases.sql` file, you have to execute the following commands:
```` shell script
docker exec -it --user postgres tf-database psql -c "CREATE ROLE martine WITH LOGIN PASSWORD 'martine' NOSUPERUSER INHERIT NOCREATEDB NOCREATEROLE NOREPLICATION"
docker exec -it --user postgres tf-database psql -c "CREATE ROLE chef WITH LOGIN PASSWORD 'chef' NOSUPERUSER INHERIT NOCREATEDB NOCREATEROLE NOREPLICATION"
docker exec -it --user postgres tf-database psql -c "CREATE ROLE chronos WITH LOGIN PASSWORD 'chronos' NOSUPERUSER INHERIT NOCREATEDB NOCREATEROLE NOREPLICATION"
````
## Stop dev infra
Once all the containers are up and running, you can shut them down with the commands:
````shell script
$ docker-compose -f docker-compose.yaml down
$ docker-compose -f docker-compose.yaml rm
````
