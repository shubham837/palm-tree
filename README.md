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
6) Cassandra for Persistence  </br>
7) Solr for Searching </br>
8) Redis for caching </br>

# Setup instructions
This application can be run in standalone environment or inside a container on the machine.
Setup instructions are mentioned below

## Standalone Environment (Manually)
1) Install maven build tool and jdk1.8 </br>
2) Clone the repository and pull the latest change. </br>
3) Install apache Cassandra < 3.0 (use version 2.2.7) and solr. </br>
4) Start Cassandra Server with command `sudo <cassandra_dir>/bin/cassandra start`  </br>
5) Create Keyspace in cassandra with command `<cassandra_dir>/bin/cqlsh -f <source_dir>/compose/cassandra/database_creation.cql` </br>
6) Start Solr Server with command `<solr_dir>/bin/solr start`  </br>
7) Create Solr Core with command  `<solr_dir>/bin/solr create_core -c crossover` </br>
8) Create Solr Schema with command `cp <source_dir>/compose/solr/managed-schema <solr_dir>/server/solr/crossover/conf/`  </br>
9) Restart solr server to pick the created schema with command `<solr_dir>/bin/solr restart`  </br>
10) Build the application using the command `mvn clean install`. This step would run test cases too.</br>
  Jar file is created in folder `/Users/<user_name>/.m2/repository/com/crossover/crossover/1.0-SNAPSHOT/crossover-1.0-SNAPSHOT.jar` </br>
11) Run the jar file using the command `java -jar /Users/<user_name>/.m2/repository/com/crossover/crossover/1.0-SNAPSHOT/crossover-1.0-SNAPSHOT.jar` </br>
12) Application is ready to serve the user on IP: 127.0.0.1 and port:8080

## Using Docker
1) Install and Initialize docker-machine using link https://docs.docker.com/machine/get-started/ </br>
2) After installation set the environment variable for docker machine `eval "$(docker-machine env default)"` </br>
3) Run test cases with command `make test` </br>
4) Run the application with command `make setup` </br>
5) Application is ready to serve the user on IP: `docker-machine ip` (192.168.99.100) and port:8080 </br>


# Usage:
1) API's Endpoints Exposed in web application are: </br>
   a) HTTP POST /rental-info </br>
   b) HTTP PUT  /rental-info/{rental_info_id}  </br>
   c) HTTP GET  /rental-info/{rental_info_id} </br>
   d) HTTP GET  /rental-info?general_term=xyz&type=Villa&city=SanMateo  </br>
