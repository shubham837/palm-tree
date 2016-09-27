# furry-barnacle

### Brief
API Client to fetch the city info from GoEURO api and save it to csv file.</br>
This rental-info Application is easily extensible for other logics and adding caching and persistence layer.

Technologies/Library used in rental-info Application:<br/>
1) jdk-1.8 </br>
2) Maven as build Tool </br>
3) Spring-boot </br>
4) gson for Deserialization </br>
5) Apache HttpComponents </br>


# Setup instructions
1) Install maven build tool and jdk1.8 </br>
2) Clone the repository and pull the latest change. </br>
3) Build the application using the command `mvn clean install`</br>
  Jar file is created in folder '/Users/<user_name>/.m2/repository/com/goeuro/position/city-info/1.0-SNAPSHOT/city-info-1.0-SNAPSHOT.jar' </br>
4) Run the jar file using the command `java -jar /Users/shubham.singhal/.m2/repository/com/goeuro/position/city-info/1.0-SNAPSHOT/city-info-1.0-SNAPSHOT.jar berlin` </br>
5) csv file is created in folder '/tmp/<city_name>_info.csv </br>


# Solr configurations
1)