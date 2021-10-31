# Parking API
REST API aims to list the available parking lots based on location.

# Features
It relies on villages public API to retrieve the needed information. It provides certain features to manage different resources:

*CRUD functionalities to manage the SIRET numbers and business information
"Find" endpoint to retrieve the parking lots available on real time
SWAGGER documentation for the provided endpoints.

# Tech
Business Microservice uses a number of open source projects/technologies to work properly:

Maven
Spring (Boot, Actuator, JPA, Data Rest, Boot Test)
Springfox for Swagger generation
Jackson for JSON Mapping support (for unmarshalling)
Lombok - Boilerplate code reduction
SLJ4J - Logging
JUnit

# Structure
The project is structured as a Maven Project. By default the project uses a H2 in-memory DB, this can be changed by modifying the application.properties file. JPA Repositories are being used to abstract the data storage interactions. For the sake of simplicity, the implementation of the CRUD controllers is defined in the Spring Data Rest way, using the RepositoryRestResource annotation, directly in the repository interfaces. A custom keystore (Keystore.jks) is used, this contains the trusted certificates which the http client (Spring RestTemplate) is using to query external resources (ex Sirene API) The application can be packed in a Docker image. For that we have the Dockerfile that describes the image to be delivered. It contains a Jenkinsfile to describe the declarative pipeline for the Jenkins CICD. For now stages for image push, QA tests, deployments are left as TODOs. A very minimal workflow that integrates well with GitHub ecosystem (running just the package).
