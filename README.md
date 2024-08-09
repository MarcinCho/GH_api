# Github public repositories client
Simple API service to check user public repositories and branches.
## How to run
**Requierments**  
Java 21  
Maven >= 3.8.6

1. Clone the repository
```bash
git clone https://github.com/MarcinCho/gh_api.git

```
2. Change working directory
```bash
cd atipera/
```
3. Run program  
Dev mode or package it and run from jar
```bash
# dev mode
./mvnw compile quarkus:dev

# Package to jar
./mvnw package -Dquarkus.package.jar.type=uber-jar

# Run jar
./mvnw package -Dquarkus.package.jar.type=uber-jar
```
Create Docker image:
```bash
# first create native executable with
./mvnw package -Dnative -Dquarkus.native.container-build=true

# next create Docker img
docker build -f src/main/docker/Dockerfile.native -t quarkus/gh_api .

# run Docker img
docker run -i --rm -p 8080:8080 quarkus/gh_api
```


4. Application starts at http://localhost:8080
5. To get repositories of given user use path http://localhost:8080/api/{username}
6. OpenAPI documentation is availlible a http://localhost:8080/q/swagger-ui  
   Expected outcomes are provided within documentation.


<font color="red">If response has a 403 status code, please read github documentation</font>  
https://docs.github.com/rest/overview/resources-in-the-rest-api#rate-limiting


