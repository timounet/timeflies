# Development requirements
## Git
- Branch names convention : [GitFlow names](https://nvie.com/posts/a-successful-git-branching-model/)
- Feature branches : use Pull Request 
## Checking Ports
In development, we will use several ports. Just make sure the following ports are free so you don’t run into any conflicts
````shell script
$ lsof -i tcp:8080    // UI
$ lsof -i tcp:8081    // GW api
$ lsof -i tcp:8082    // Timer REST API
$ lsof -i tcp:8083    // User REST API
$ lsof -i tcp:8084    // Project REST API
$ lsof -i tcp:5432    // Postgres
$ lsof -i tcp:9090    // Prometheus
$ lsof -i tcp:2181    // Zookeeper
$ lsof -i tcp:9092    // Kafka
$ lsof -i tcp:8443    // Keycloak
$ lsof -i tcp:443     // nginx
$ lsof -i tcp:80 ?    // nginx
````
## Software
### windows
Use [chocolatey](https://chocolatey.org/) in powershell (admin mode)
````shell script
choco install -y         `
curl	                 `
dep	                     `
docker-desktop	         `
git.install	             `
graalvm	                 `
jq	                     `
kubernetes-cli	         `
kubernetes-helm	         `
maven	                 `
yarn                     `
nodejs.install	         `
vcredist2010	         `
visualcpp-build-tools	 `
Wget	
````
Recommended tools
````shell script
choco install -y         `
discord.install	         `
intellijidea-community	 `
postman	                 `
scite	
````
### mac OS X
Use [homebrew](https://brew.sh/) 
`````shell script
brew install  -y    \
curl	            \
dep	                \
docker	            \
git	                \
graalvm-ce-java11	\
jq	                \
kubernetes-cli	    \
helm	            \
maven	            \
node	            \
yarn                \
Wget	
`````
Recommended tools
````shell script
brew install -y     \
discord	            \
intellijidea-ce	    \
postman	 
````
### Details
#### GraalVM
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
### Docker
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

### Recap
Just make sure the following commands work on your machine
`````shell script
$ java -version
$ $GRAALVM_HOME/bin/native-image --version
$ mvn -version
$ curl --version
$ docker version
$ docker-compose version
`````