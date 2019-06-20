# WAES Assessment Application
Candidate: Daniel Chiuratto Seabra

## Running the Application
The application is implemented using the Java 8, but it works for more recent versions of Java without issues.

So, to run the application:
* Clone the application using git clone with the address:
```
git clone https://github.com/daniel-chiuratto-seabra/waes.git
``` 
* Once cloned, access the project root folder and to execute the application you can choose two approaches: using the package goal from Maven to run the tests, pack it as a JAR and run it through the Java command or using the Spring Boot plugin which executes the application directly without executing the tests:

Without tests:
```
mvn spring-boot:run
```

With tests:
```
mvn package
```
And then execute:
```
java -jar target/assessment-0.0.1-SNAPSHOT.jar
```
## Application Context Name
The application does not contain a defined context name, so to reach it through Rest requests, running it locally, you can do like the example below with the id 10:
```
POST: http://localhost:8080/v1/diff/10/left
POST: http://localhost:8080/v1/diff/10/right
GET: http://localhost:8080/v1/diff/10
```
## Testing the Application
According to the requirement, the application should accept Base64 encoded binary data, for the testing you can check it through the Spring Boot Test, executing it through the JUnit Test class **AssessmentApplicationTests** class executing it through the IDE of your preference or executing it via command line with the **mvn test** command through the root folder of the project which will start the application and doing the requests where you can follow-up through logging and results on the tests itself, or through a Rest client like Postman for instance, together with any utilitary that helps to translate a String into Base64 encoded String.

Using an external client you can do **POST** and **GET** calls through the endpoints defined on the requirement, sending raw data with Base64 encoded data as String. To have this encoded data you can use online solutions like the website **https://www.base64encode.org**, or if you are on Mac, you can use the terminal entering the following command:
```
echo "{\"field\":\"this is a sample\"}" | base64
```
And copy the result of this process to the body of the Rest client to be sent to the application and then processed by it.

## Application Structure
The application is divided in:
* Controller class
* Service class
* Utility class
* Exception Handler class

The Controller class (AssessmentApplicationController) is the interface with the outside world and has the function to receive the payload from the outside, sending it to the Service.

The Service class (AssessmentApplicationService and MessageService) is where the business logic are, also consuming other Services and the Utilitary classes to do the work, returning the result to the Controller.

The Utility class (JsonUtil) is focused in giving support for certain functionalities in the application that is complex enough to justify to have it in another class.

The Exception Handler class is where all exceptions thrown during the application goes if they happen, because they are handled to have a more standarized way to be returned to requestor with a friendly message.

## Improvements
### Caching
It was implemented a **Map** which caches the result of the process (when we do a **GET**) in a manner that when we repeat such call without setting anything to the same id, instead of the application re-run all the processes, it gets the result that was cached on a prior execution. This approach optimizes the performance.
### Diff Result
To attend the scenario of having two payloads with the same id, having the same size but different values, it was implemented an utilitary class (JsonUtil) which finds the difference between the two payloads returning with a certain amount of detail what fields and their values to allow the requestor to know exactly what is the difference between them, despite their equality in size.
