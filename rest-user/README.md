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
## Quarkus Creation
Created with maven template:
`````shell script
mvn io.quarkus:quarkus-maven-plugin:1.3.2.Final:create \
    -DprojectGroupId=org.timeflies \
    -DprojectArtifactId=rest-user \
    -DclassName="org.timeflies.user.UserResource" \
    -Dpath="api/users" \
    -Dextensions="kotlin,jdbc-postgresql,hibernate-orm-panache,hibernate-validator,resteasy-jsonb,openapi,health,metrics"
`````
Or can be imported thanks to [code.quarkus.oi](https://code.quarkus.oi)
![Quarkus Project Generator](../doc/rest-user-quarkus-extension.png)

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```
./mvnw quarkus:dev
```

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