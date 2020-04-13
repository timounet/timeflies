# rest-user project

This project uses Quarkus, the Supersonic Subatomic Java Framework.
If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .

## Quarkus Extensions
- kotlin
- jdbc-postgresql
- hibernate-orm-panache
- hibernate-validator
- resteasy-jsonb
- openapi
- health 
- metrics

## Bootstrapping the User REST Endpoint
Created with maven archetype template:
`````shell script
mvn io.quarkus:quarkus-maven-plugin:1.3.2.Final:create \
    -DprojectGroupId=org.timeflies \
    -DprojectArtifactId=rest-user \
    -DclassName="org.timeflies.users.UserResource" \
    -Dpath="api/users" \
    -Dextensions="kotlin,jdbc-postgresql,hibernate-orm-panache,hibernate-validator,resteasy-jsonb,openapi,health,metrics"
`````
Or can be imported thanks to [code.quarkus.oi](https://code.quarkus.oi)
![Quarkus Project Generator](../doc/rest-user-quarkus-extension.png)

## Directory Structure

The Maven archetype generates the following rest-user sub-directory:
- the Maven structure with a pom.xml
- an org.timeflies.users.UserResource resource exposed on /api/users
- an associated unit test UserResourceTest
- the landing page index.html that is accessible on http://localhost:8080 after starting the application
- example Dockerfile files for both native and jvm modes in src/main/docker. Would be replaced by jib later
- the application.properties configuration file

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```
./mvnw quarkus:dev
```
Then check that the endpoint returns hello as expected:
````
$ curl http://localhost:8080/api/users
hello
````
Alternatively, you can open http://localhost:8080/api/users in your browser

## Transactions and ORM
   
The User API’s role is to allow CRUD operations on timeflies users. In this module we will create a User entity and persist/update/delete/retrieve it from a Postgres database in a transactional way.
You should already have installed the infrastructure into the infrastructure directory. Now, just execute `docker-compose -f docker-compose.yaml up -d`. You should see a few logs going on and then all the containers get started.
[Infra more info](../infrastructure/README.md)

### User Entity
To define a Panache entity, simply extend PanacheEntity, annotate it with @Entity and add your columns as public fields (no need to have getters and setters)

## Packaging and running the application

The application can be packaged using `./mvnw package`.
It produces the `rest-user-1.0-SNAPSHOT-runner.jar` file in the `/target` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/lib` directory.

The application is now runnable using `java -jar target/rest-user-1.0-SNAPSHOT-runner.jar`.

## Creating a native executable

You can create a native executable using: `./mvnw package -Pnative`.

Or, if you don't have GraalVM installed, you can run the native executable build in a container using: `./mvnw package -Pnative -Dquarkus.native.container-build=true`.

You can then execute your native executable with: `./target/rest-user-1.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/building-native-image.