# timeflies
App for sub contractors who works for several account at the same time and who would love to measure their workload to ease creation and justification invoices. A quarkus micro service application based on soyeloso/timeflies concept. 

## Global project overview
Application that allows to track time to projects. It's about microservices communicating either synchronously via REST or asynchronously using Kafka:
![Image of micro services diagram](./doc/micro-services-diagram.png)
- [User REST API](./rest-user/README.md): Allows CRUD operations on User which are stored in a Postgres database [![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=timounet_timeflies_rest-users&metric=alert_status)](https://sonarcloud.io/dashboard?id=timounet_timeflies_rest-users) ![Rest User Profile CI](https://github.com/timounet/timeflies/workflows/Rest%20User%20Profile%20CI/badge.svg?branch=develop)
- [Project REST API](./rest-project/README.md): Allows CRUD operations on Project which are stored in a Postgres database
- [Timer REST API](./rest-timer/README.md): This REST API invokes the User and Project APIs to start / stop a clock timer on a project. Each timer is stored in a Postgres database
- [Timeflies UI](./ui/README.md): an Angular? application allowing you to pick up a project and play timers. The TimeFlies UI is exposed via Quarkus and invokes the Timer REST API
- [Statistics](./event-statistics/README.md): Each timer is asynchronously sent (via Kafka) to the Statistics microservice. It has a HTML + JQuery UI displaying all the statistics.
- Prometheus polls metrics from the three microservices
## Development requirements
### Git
- Branch names convention : [GitFlow names](https://nvie.com/posts/a-successful-git-branching-model/)
- Feature branches : use Pull Request 
### Checking Ports
In development, we will use several ports. Just make sure the following ports are free so you don’t run into any conflicts
````sh
$ lsof -i tcp:8080    // UI
$ lsof -i tcp:8082    // Timer REST API
$ lsof -i tcp:8083    // User REST API
$ lsof -i tcp:8084    // Project REST API
$ lsof -i tcp:5432    // Postgres
$ lsof -i tcp:9090    // Prometheus
$ lsof -i tcp:2181    // Zookeeper
$ lsof -i tcp:9092    // Kafka
````
### Software
#### windows
Use [chocolatey](https://chocolatey.org/) in powershell (admin mode)
````batch
choco install -y `
curl	 `
dep	 `
docker-desktop	 `
git.install	 `
graalvm	 `
jq	 `
kubernetes-cli	 `
kubernetes-helm	 `
maven	 `
nodejs.install	 `
vcredist2010	 `
visualcpp-build-tools	 `
Wget	
````
Recommended tools
````batch
choco install -y `
discord.install	 `
intellijidea-community	 `
postman	 `
scite	
````
#### mac OS X
Use [homebrew](https://brew.sh/) 
`````shell script
brew install  -y \
curl	 \
dep	 \
docker	 \
git	 \
graalvm-ce-java11	 \
jq	 \
kubernetes-cli	 \
helm	 \
maven	 \
node	 \
Wget	
`````
Recommended tools
````shell script
brew install -y \
discord	 \
intellijidea-ce	 \
postman	 
````
#### Details
##### GraalVM
GraalVM is an extension of the Java Virtual Machine (JVM) to support more languages and several execution modes. It supports a large set of languages: Java of course, other JVM-based languages (such as Groovy, Kotlin etc.) but also JavaScript, Ruby, Python, R and C/C++. It includes a new high performance Java compiler, itself called Graal, which can be used in a Just-In-Time (JIT) configuration on the HotSpot VM, or in an Ahead-Of-Time (AOT) configuration on the Substrate VM. One objective of Graal is to improve the performance of Java virtual machine-based languages to match the performance of native languages.
````shell script
# Checking Graalvm - Set properly GRAALVM_HOME environment variable
$ java -version
openjdk version "11.0.6" 2020-01-14
OpenJDK Runtime Environment GraalVM CE 20.0.0 (build 11.0.6+9-jvmci-20.0-b02)
OpenJDK 64-Bit Server VM GraalVM CE 20.0.0 (build 11.0.6+9-jvmci-20.0-b02, mixed mode, sharing)
$ javac -version
javac 11.0.6
````
Install Native images. Go to GRAALVM_HOME directory and run
````shell script
$ gu install native-image
# Checking for GraalVM Installation
$ $GRAALVM_HOME/bin/native-image --version
GraalVM Version 20.0.0 CE
````
#### Docker
Checking for Docker Installation
Once installed, check that both docker and docker-compose are available in your PATH:
````shell script
$ docker --version
Docker version 19.03.8, build afacb8b
$ docker-compose --version
docker-compose version 1.25.4, build 8d51620a
$ docker run hello-world
Hello from Docker!
...
````
Open Docker Settings control pannel in General set:
- Expose daemon on tcp://localhost:2375 without TLS
In Kubernetes menu set:
- Enable Kubernetes
- Deploy Docker Stacks to Kubernetes by default
````shell script
$ kubectl version
Client Version: version.Info{Major:"1", Minor:"18", GitVersion:"v1.18.0", GitCommit:"9e991415386e4cf155a24b1da15becaa390438d8", GitTreeState:"clean", BuildDate:"2020-03-25T14:58:59Z", GoVersion:"go1.13.8", Compiler:"gc", Platform:"windows/amd64"}
Server Version: version.Info{Major:"1", Minor:"15", GitVersion:"v1.15.5", GitCommit:"20c265fef0741dd71a66480e35bd69f18351daea", GitTreeState:"clean", BuildDate:"2019-10-15T19:07:57Z", GoVersion:"go1.12.10", Compiler:"gc", Platform:"linux/amd64"}
````
In Resource menu, if you want to build native images you should set Memory at 6GB at least

#### Recap
Just make sure the following commands work on your machine
`````shell script
$ java -version
$ $GRAALVM_HOME/bin/native-image --version
$ mvn -version
$ curl --version
$ docker version
$ docker-compose version
`````

### Warming up Maven
### Warming up Docker
What’s this infra?
Any microservice system is going to rely on a set of technical services. In our context, we are going to use PostgreSQL as the database, Prometheus as the monitoring tool, and Kafka as the event/message bus. This infrastructure starts all these services, so you don’t have to worry about them.
To warm up your Docker image repository, navigate to the `infrastructure` directory. Here, you will find a `docker-compose.yaml`/`docker-compose-linux.yaml` files which defines all the needed Docker images. Notice that there is a `db-init` directory with a `initialize-databases.sql` script which sets up our databases and a `monitoring` directory (all that will be explained later).
In a short way
`````shell script
# On non Linux environment run
$ docker-compose -f docker-compose.yaml up -d
# On linux run
$ docker-compose -f docker-compose-linux.yaml up -d
`````
This will download all the Docker images and start the containers.
Once all the containers are up and running, you can shut them down with the commands:
````shell script
$ docker-compose -f docker-compose.yaml down
$ docker-compose -f docker-compose.yaml rm
````
more details in :
[Development Infrastructure](./infrastructure/README.md) :grey_exclamation:

### Cloud and deployments
[Kubernetes](./kubernetes/README.md) :grey_exclamation:

## References
this project is inspired by [Quarkus super heroes workshop](https://quarkus.io/quarkus-workshops/super-heroes/)

 

