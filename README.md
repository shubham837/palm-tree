# CrossOver

### Brief
Web Application to provide rental information to users. Using this Application
users would be able to save and search rental information realtime.

###
Technologies/Library used in rental-info Application:<br/>
1) jdk-1.8 </br>
2) Maven as build Tool </br>
3) Spring-boot </br>
4) gson for Deserialization </br>
5) Apache HttpComponents </br>
6) Cassandra for Persistence
7) Solr for Searching
8) Redis for caching

# Setup instructions
This application can be run in standalone environment or inside a container on the machine.
Setup instructions are mentioned below

## Standalone Environment (Manually)
1) Install maven build tool and jdk1.8 </br>
2) Clone the repository and pull the latest change. </br>
3) Build the application using the command `mvn clean install`. This step would run test cases too.</br>
  Jar file is created in folder '/Users/<user_name>/.m2/repository/com/crossover/crossover/1.0-SNAPSHOT/crossover-1.0-SNAPSHOT.jar' </br>
4) Run the jar file using the command `java -jar /Users/<user_name>/.m2/repository/com/crossover/crossover/1.0-SNAPSHOT/crossover-1.0-SNAPSHOT.jar` </br>

## Using Docker
1) Install and Initialize docker-machine using link https://docs.docker.com/machine/get-started/ </br>
2) After installation set the environment variable for docker machine `eval "$(docker-machine env default)"`
3) Run test cases with command `make test`
4) Run the application with command `make setup`
