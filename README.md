### Vodafone IOT Application

Follow below steps to start the application and test it.

### Starting the application : 

1. Extract the zip file and import the project into the IDE (STS/Eclipse/IntelliJ) as existing maven project.

2. Run the command "mvn clean install" (Run As Maven Build).

3. Run the application as Spring Boot Application. 

4. The application will start on the port 8080.

### Testing the application : 

1. Import the postman collection in the project to you postman application. (Sent separately in mail and also present in the project "Vodafone Iot.postman_collection")

2. Once the application is up and running, hit the /load-data endpoint from postman to load the data.

3. You can test different scenarios by not providing filepath, file not present in provided filepath, file don't have any data etc (for requirement 1.1.1)

4. Once data is loaded, you can test the /status endpoint for productId and timeStamp for scenarios like, productId not present, getting status of product, if three consecutive location is same then status is inactive for cycle plus tracker etc (for requirement 1.1.2 and 1.1.3)

5. You can find few scenarios for requirement 1.1.1, 1.1.2 and 1.1.3 tested and documented by me in the document sent in mail separately.

6. Alternatively you can test the application from Swagger UI at http://localhost:8080/swagger-ui/index.html



### Modifications

1. For requirement 1.1.1 the endpoint is modified from /iot/event/v1/ to /iot/event/v1/load-data

2. For requirement 1.1.1 the endpoint is modified from /iot/event/v1?ProductId=WG11155638?tstmp=1582605137000 to /iot/event/v1/status?ProductId=WG11155638?tstmp=1582605137000

3. Make sure that the all values in DataTime column are numbers and not represented as math formula as in case of excel

4. Few custom exceptions with custom error message are added other than given in problem statement.

5. Repository layer is implemented to simulate actual production scenario but the data is stored in java inbuild collection i.e. List as mentioned in the problem statement.