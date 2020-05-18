# rest-gw-web project

# webapp project

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```
./mvnw quarkus:dev
```

## Packaging and running the application

The application can be packaged using `./mvnw package`.
It produces the `rest-gw-web-1.0-SNAPSHOT-runner.jar` file in the `/target` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/lib` directory.

The application is now runnable using `java -jar target/rest-gw-web-1.0-SNAPSHOT-runner.jar`.

## Creating a native executable

You can create a native executable using: `./mvnw package -Pnative`.

Or, if you don't have GraalVM installed, you can run the native executable build in a container using: `./mvnw package -Pnative -Dquarkus.native.container-build=true`.

You can then execute your native executable with: `./target/rest-gw-web-1.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/building-native-image.
:warning: Integration with keycloak is ssl. Every requirements specified [Using SSL with native executables](https://quarkus.io/guides/native-and-ssl) need to be satisfied. Don't know yet how to solve the "The TrustStore path" in case of jib containerization

#### Web app creation
````shell script
mvn io.quarkus:quarkus-maven-plugin:1.4.2.Final:create ^
    -DprojectGroupId=org.timeflies ^
    -DprojectArtifactId=rest-gw-web ^
    -Dextensions="oidc, keycloak-authorization, resteasy-jsonb,rest-client,smallrye-openapi,health,metrics,container-image-jib"
````

### Container Images
The extension quarkus-container-image-jib is powered by Jib for performing container image builds. The major benefit of using Jib with Quarkus is that all the dependencies (everything found under target/lib) are cached in a different layer than the actual application making rebuilds really fast and small (when it comes to pushing). Another important benefit of using this extension is that it provides the ability to create a container image without having to have any dedicated client side tooling (like Docker) or running daemon processes (like the Docker daemon) when all that is needed is the ability to push to a container image registry.

In situations where all that is needed to build a container image and no push to a registry is necessary (essentially by having set `quarkus.container-image.build=true` and left `quarkus.container-image.push` unset - it defaults to false), then this extension creates a container image and registers it with the Docker daemon. This means that although Docker isn’t used to build the image, it is nevertheless necessary. Also note that using this mode, the built container image will show up when executing docker images. 
#### Building

To build a container image for your project, `quarkus.container-image.build=true` needs to be set using any of the ways that Quarkus supports.
````shell script
mvnw clean package -Dquarkus.container-image.build=true

For native:
````shell script
mvnw clean package -Pnative -Dquarkus.native.container-build=true 
````
#### Pushing

To push a container image for your project, `quarkus.container-image.push=true` needs to be set using any of the ways that Quarkus supports.
````shell script
mvnw clean package  -Dquarkus.container-build=true -Dquarkus.container-image.push=true 
````
For native:
:warning: Integration with keycloak is ssl. Every requirements specified [Using SSL with native executables](https://quarkus.io/guides/native-and-ssl) need to be satisfied. Don't know yet how to solve the "The TrustStore path" in case of jib containerization
````shell script
mvnw clean package -Pnative -Dquarkus.native.container-build=true -Dquarkus.container-image.push=true -Dquarkus.container-image.tag=native-${quarkus.application.version:latest}
````
If no registry is set (using `quarkus.container-image.registry`) then [docker.io](https://hub.docker.com/) will be used as the default.


#### Run
````shell script
docker run --rm -p 8081:8081  timefliesapp/gw-web:1.0-SNAPSHOT .
````


##### How to connect: Testing the Application
The application is using bearer token authorization and the first thing to do is obtain an access token from the Keycloak Server in order to access the application resources:
````shell script
export access_token=$(\
    curl -X POST http://localhost:8080/auth/realms/quarkus/protocol/openid-connect/token \
    --user backend-service:secret \
    -H 'content-type: application/x-www-form-urlencoded' \
    -d 'username=alice&password=alice&grant_type=password' | jq --raw-output '.access_token' \
 )
````


The example above obtains an access token for user alice.

Any user is allowed to access the http://localhost:8081/api/users/me endpoint which basically returns a JSON payload with details about the user.

````shell script
 curl -v -X GET \
  http://localhost:8081/api/users/me \
  -H "Authorization: Bearer "$access_token
````
The http://localhost:8081/api/admin endpoint can only be accessed by users with the admin role. If you try to access this endpoint with the previously issued access token, you should get a 403 response from the server.
````shell script
 curl -v -X GET \
    http://localhost:8081/api/admin \
    -H "Authorization: Bearer "$access_token
````
In order to access the admin endpoint you should obtain a token for the admin user:
````shell script
export access_token=$(\
    curl -X POST http://localhost:8080/auth/realms/quarkus/protocol/openid-connect/token \
    --user backend-service:secret \
    -H 'content-type: application/x-www-form-urlencoded' \
    -d 'username=admin&password=admin&grant_type=password' | jq --raw-output '.access_token' \
 )
````

