# timeflies
App for sub contractors who works for several account at the same time and who would love to measure their workload to ease creation and justification invoices. A quarkus micro service application based on soyeloso/timeflies concept. 

## Global project overview
Application that allows to track time to projects. It's about microservices communicating either synchronously via REST or asynchronously using Kafka:
![Image of micro services diagram](./doc/micro-services-diagram.png)
- [User REST API](./rest-user/README.md): Allows CRUD operations on User which are stored in a Postgres database
- [Project REST API](./rest-project/README.md): Allows CRUD operations on Project which are stored in a Postgres database
- [Timer REST API](./rest-timer/README.md): This REST API invokes the User and Project APIs to start / stop a clock timer on a project. Each timer is stored in a Postgres database
- [Timeflies UI](./ui/README.md): an Angular? application allowing you to pick up a project and play timers. The TimeFlies UI is exposed via Quarkus and invokes the Timer REST API
- [Statistics](./event-statistics/README.md): Each timer is asynchronously sent (via Kafka) to the Statistics microservice. It has a HTML + JQuery UI displaying all the statistics.
- Prometheus polls metrics from the three microservices
## Development requirements
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
### Checking Ports
### Warming up Maven
### Warming up Docker
[Development Infrastructure](./infrastructure/README.md) :grey_exclamation:

### Cloud and deployments
[Kubernetes](./kubernetes/README.md) :grey_exclamation:

## References
this project is inspired by [Quarkus super heroes workshop](https://quarkus.io/quarkus-workshops/super-heroes/)

 

