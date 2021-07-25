# Chattifier-V2

Chattifier is a backend for a distributed chat messaging system running inside the Kubernetes cluster.
 - Microservice architecture
 - Purely event-based and asynchronous
 - Some architectural improvements were made compared to the [first version](https://github.com/bytestreme/chattifier-backend)
   - Migrated from multiple sockets to one centralized socket for all in/out events
 
# Tech stack

The following stack was used for this project

 - Spring Framework - core ecosystem
	 - Spring Cloud - microservices communication
	 - Spring Security - authentication, authorization
 - Apache Cassandra - distributed storage (users, message, rooms)
 - Redis - in-memory storage (token versions, connected users)
 - Apache Pulsar - core event streaming platform
 - Netty - each microservice runs on Netty (comes with Spring WebFlux) allowing to create a continuous stream from request to the database call
 - Project Reactor - reactive library (based on the [Reactive Streams](https://github.com/reactive-streams/reactive-streams-jvm) ) 
# Project modules

Structurally, the project is a Gradle project with multiple sub-projects

## Config server
Holds configuration properties of the microservices. Git repository is used as a source. Updating the repository will trigger microservices configuration update at runtime.
## Service registry - Netflix Eureka
Applications register themselves on startup. Allows the services to discover each other
## Cloud gateway
The entry point to the system. Proxying and load-balancing. Requests authorization
## Pulsar Data
Contains data models for the message broker. Allows to import the modules and reuse them across different projects
## Socket API
WebSocket connection for input requests and output server responses
When connected user id is stored in the Redis store (used for chat service) 
## Chat service
Handling incoming messages and sending them to the users in the same chatting room.
If a user is connected then the output event is produced.
## Message service
Persists incoming messages in the Apache Cassandra database
## User service
User sign up, sign in
# Deployment
## Build & Push
Each Gradle project uses [Gradle Docker Plugin](https://github.com/palantir/gradle-docker)
Running dockerPush task builds and pushes the Docker images to the registry
## Run
k8s folder contains Kubernetes deployment configurations. The project can be deployed by running

    kubectl apply -f ./

