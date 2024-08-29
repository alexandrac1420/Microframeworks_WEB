# Web Framework for REST Services and Static File Management

This project enhances a basic web server to support RESTful services and static file management. The server is designed to handle multiple client requests concurrently, utilizing a thread pool for efficient processing of both static files

![Demo GIF](https://github.com/alexandrac1420/Aplicaciones_Distribuidas/blob/master/out/diagrama/Dise%C3%B1o%20sin%20t%C3%ADtulo.gif)


## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites

You need to install the following tools and configure their dependencies:

1. **Java** (versions 7 or 8)
    ```sh
    java -version
    ```
    Should return something like:
    ```sh
    java version "1.8.0"
    Java(TM) SE Runtime Environment (build 1.8.0-b132)
    Java HotSpot(TM) 64-Bit Server VM (build 25.0-b70, mixed mode)
    ```

2. **Maven**
    - Download Maven from [here](http://maven.apache.org/download.html)
    - Follow the installation instructions [here](http://maven.apache.org/download.html#Installation)

    Verify the installation:
    ```sh
    mvn -version
    ```
    Should return something like:
    ```sh
    Apache Maven 3.2.5 (12a6b3acb947671f09b81f49094c53f426d8cea1; 2014-12-14T12:29:23-05:00)
    Maven home: /Users/dnielben/Applications/apache-maven-3.2.5
    Java version: 1.8.0, vendor: Oracle Corporation
    Java home: /Library/Java/JavaVirtualMachines/jdk1.8.0.jdk/Contents/Home/jre
    Default locale: es_ES, platform encoding: UTF-8
    OS name: "mac os x", version: "10.10.1", arch: "x86_64", family: "mac"
    ```

3. **Git**
    - Install Git by following the instructions [here](http://git-scm.com/book/en/v2/Getting-Started-Installing-Git)

    Verify the installation:
    ```sh
    git --version
    ```
    Should return something like:
    ```sh
    git version 2.2.1
    ```

### Installing

1. Clone the repository and navigate into the project directory:
    ```sh
    git clone https://github.com/alexandrac1420/Microframeworks_WEB.git

    cd Microframeworks_WEB
    ```

2. Build the project:
    ```sh
    mvn package
    ```

    Should display output similar to:
    ```sh
      [INFO] --- jar:3.3.0:jar (default-jar) @ Spark ---
      [INFO] Building jar: C:\Users\alexa\OneDrive\Escritorio\Microframeworks_WEB\target\Spark-1.0-SNAPSHOT.jar
      [INFO] BUILD SUCCESS
    ```

3. Run the application:
    ```sh
    java -cp target/Spark-1.0-SNAPSHOT.jar edu.escuelaing.arep.SimpleWebServer 

    ```
    When running the application, the following message should appear

    ```
    Ready to receive on port 8080...
    ```

    And now you can access `index.html` and other static files in http://localhost:8080/index.html

### Usage

1. **Static Files**: Access `index.html` and other static files (CSS, JavaScript, images) by navigating to `http://localhost:8080/index.html` in your web browser.

2. **Dynamic Greeting**:
   - **GET Request**: Open `index.html` in your browser. In the "GET Request" section, type a name into the provided input field and click the "Get Input" button. This action sends a GET request to `/app/hello?name=<YourName>`. The server will respond with:
     - **Plain Text**: A greeting message like `Hola, <YourName>`. For example, if you input "World", the response will be `Hola, World`.
     - **JSON**: A JSON object with the greeting message, such as `{"nombre": "<YourName>"}`. For instance, for the name "World", the response will be `{"nombre": "World"}`.

3. **POST Request**:
   - **POST Request**: In the "POST Request" section of `index.html`, type a name into the input field and click the "Post Input" button. This sends a POST request to `/app/hellopost?name=<YourName>`. The server will respond with:
     - **Plain Text**: A message indicating the received input, like `Post received: <YourName>`. For example, if you input "Jose Mario", the response will be `Post received: Jose Mario`.
     - **JSON**: A JSON object with the received name, for instance, `{"name": "<YourName>"}`. If you input "Jose Mario", the response will be `{"name": "Jose Mario"}`.

    
## Architecture

![Architecture Diagram](https://github.com/alexandrac1420/Microframeworks_WEB/blob/master/Pictures/Arquitectura.png)

### Overview

The Web Framework is designed to handle HTTP client requests, serve static files, and process REST API requests efficiently. The architecture enables seamless interaction between the client, the browser, and the backend server.

### Components

#### 1. **Client**
   - **Role**: Represents the end-user who interacts with the application through a web browser.
   - **Responsibilities**:
     - Send HTTP requests via the browser to the server.
     - Receive and display HTTP responses, including web pages and API results.

#### 2. **Browser**
   - **Role**: Serves as the interface through which the client interacts with the web server.
   - **Components**:
     - **index.html**: The main HTML file that structures the web content.
     - **style.css**: The CSS file that styles the content.
     - **script.js**: The JavaScript file that adds interactivity.
     - **image.png**: Example of an image that can be displayed on the page.
   - **Responsibilities**:
     - Load and render the web content (HTML, CSS, JS, images).
     - Send requests for static files or REST API calls to the backend server.

#### 3. **Backend Server**
   - **Role**: Processes client requests and serves the appropriate content or API responses.
   - **Components**:
     - **SimpleWebServer**: The core server class that manages HTTP requests and delegates tasks to appropriate handlers.
     - **Request**: Represents and parses HTTP requests from the client.
     - **Response**: Constructs and sends HTTP responses to the client.
     - **RestService**: Abstract class/interface for handling RESTful API requests.
     - **HelloService**: Concrete implementation of `RestService` that processes requests to the `/hello` endpoint.
   - **Responsibilities**:
     - Accept and parse incoming HTTP requests.
     - Serve static files (HTML, CSS, JS, images) to the client.
     - Process REST API requests and return dynamic content.
     - Manage the lifecycle of client connections and ensure proper resource allocation.

### Interaction Flow

1. **Server Initialization**: 
   - The `SimpleWebServer` starts, initializing the necessary components to handle client requests. The server listens on port 8080, waiting for incoming connections.

2. **Request Handling**:
   - The client accesses the web application via the browser, which sends an HTTP request to the `SimpleWebServer`.
   - For static files, the server retrieves the requested file (e.g., `index.html`) and sends it back to the browser.
   - For REST API requests, the server uses the `RestService` to process the request. The `HelloService` handles requests to the `/hello` endpoint.
     - In `index.html`, users can input their names, and the server will respond with a personalized greeting in JSON and plain text.
     - POST requests are also supported, where the server responds with a message like "Post received: Jose Mario."

3. **Concurrency**: 
   - The server is capable of handling multiple requests simultaneously, ensuring that several clients can interact with the server without delay.

4. **Response**:
   - The server constructs the appropriate HTTP response using the `Response` class and sends it back to the client, completing the interaction.

5. **Shutdown**:
   - When required, the server can be gracefully shut down, closing all connections and freeing resources.

This architecture ensures that both static content and dynamic RESTful services are effectively managed and delivered to the client.



## Class Diagram

![Class Diagram](https://github.com/alexandrac1420/Microframeworks_WEB/blob/master/Pictures/diagramaClases.png)

### Overview
The class diagram shows the components involved in handling HTTP requests and managing server operations. Primary classes include `SimpleWebServer`, `ClientHandler`, `Request`, `Response`, `RestService`, and `HelloService`.

### Class Descriptions

1. **SimpleWebServer**
   - **Role**: Central controller, manages server socket and delegates request handling.
   - **Key Responsibilities**:
     - **ServerSocket Management**: Maintains a `ServerSocket`.
     - **Concurrency Handling**: Uses `ExecutorService` for concurrent connections.
     - **Routing**: Routes requests to appropriate handlers.

2. **ClientHandler**
   - **Role**: Processes individual requests, handles static files and REST API requests.
   - **Key Responsibilities**:
     - **Request Handling**: Parses and processes requests.
     - **Static File Serving**: Serves files from the server.
     - **REST API Processing**: Interacts with `RestService`.

3. **Request**
   - **Role**: Represents and parses HTTP requests.
   - **Key Responsibilities**:
     - **Data Storage**: Stores method, URI, headers, and body.
     - **Data Access**: Provides methods for accessing request data.

4. **Response**
   - **Role**: Constructs and sends HTTP responses.
   - **Key Responsibilities**:
     - **Response Construction**: Sets status code, headers, and body.
     - **Data Sending**: Sends response data to the client.

5. **RestService**
   - **Role**: Abstract class/interface for RESTful services.
   - **Key Responsibilities**:
     - **Response Generation**: Provides methods for response creation.

6. **HelloService**
   - **Role**: Concrete implementation of `RestService` for `/hello` endpoint.
   - **Key Responsibilities**:
     - **Request Handling**: Responds to `/hello` requests.

## Test Report - Web Framework for REST Services and Static File Management

### Author
- **Name**: Alexandra Cortes Tovar

### Date
- **Date**: 28/08/2024

### Summary
This report outlines the unit tests conducted for the Web Framework project. Tests were performed to validate the server’s ability to handle REST services, serve static files, manage errors for missing resources, and handle multiple concurrent connections. During testing, some intermittent issues were observed where the test cases failed initially but passed on subsequent runs.

### Tests Conducted

1. **Test `testHelloServiceResponse`**
   - **Description**: Validates the server’s handling of `/hello` requests.
   - **Objective**: Ensure correct greeting response for GET requests at `/app/hello`.
   - **Testing Scenario**: Clients simulate GET requests to `/app/hello?name=World`.
   - **Expected Behavior**: Response should include `HTTP/1.1 200 OK` and JSON with greeting.
   - **Outcome**: The test initially encountered a `RejectedExecutionException` due to thread pool termination. On subsequent runs, the test passed, confirming the correct response was generated when the server thread pool was active.
   - **Verification**: Check response content for correctness.

2. **Test `testLoadStaticFile`**
   - **Description**: Checks serving of static files like `index.html`.
   - **Objective**: Validate correct serving of static files from the directory.
   - **Testing Scenario**: Request `index.html` from the server.
   - **Expected Behavior**: Response should include `HTTP/1.1 200 OK` and file content.
   - **Outcome**: Similar to the previous test, this test initially failed due to a `RejectedExecutionException`. The issue was resolved in subsequent runs, with the server correctly serving the requested file.
   - **Verification**: Confirm file is served correctly.

3. **Test `testInvalidRequest`**
   - **Description**: Tests response for non-existent files.
   - **Objective**: Ensure `404 Not Found` for missing resources.
   - **Testing Scenario**: Request a non-existent file like `nonexistentfile.html`.
   - **Expected Behavior**: Response should include `HTTP/1.1 404 Not Found` and an appropriate error message.
   - **Outcome**: Initially failed due to a `RejectedExecutionException`. The server returned the correct 404 status and error message in subsequent successful runs.
   - **Verification**: Confirm proper handling of missing resources with correct HTTP status.

4. **Test `testPostRequest`**
   - **Description**: Validates server handling of POST requests to `/app/hello`.
   - **Objective**: Ensure the server correctly processes POST requests and responds appropriately.
   - **Testing Scenario**: Send a POST request with JSON data to `/app/hello`.
   - **Expected Behavior**: The server should respond with a message acknowledging the received input.
   - **Outcome**: Initial test attempts failed due to `RejectedExecutionException`. Later runs passed, verifying that the server correctly handled the POST request when the thread pool was properly managed.
   - **Verification**: Check the response for correctness based on the provided input.

5. **Test `testMultipleConnections`**
   - **Description**: Evaluates the server’s capability to handle multiple concurrent connections.
   - **Objective**: Ensure the server can process simultaneous requests without errors or significant performance issues.
   - **Testing Scenario**: Simulate multiple clients requesting `index.html` simultaneously.
   - **Expected Behavior**: The server should handle all requests, respond with `HTTP/1.1 200 OK`, and deliver the correct content for each request.
   - **Outcome**: Initially failed due to a `RejectedExecutionException` but passed in subsequent runs. The server demonstrated the ability to manage concurrent requests effectively after the initial issues were addressed.
   - **Verification**: Verify performance and correctness under concurrent load conditions.
  
   
   ![image](https://github.com/user-attachments/assets/3de91727-e239-41a0-a73f-81a9ed00a7ab)


## Built With


* [Maven](https://maven.apache.org/) - Dependency Management
* [Git](http://git-scm.com/) - Version Control System

## Versioning

I use [GitHub](https://github.com/) for versioning. For the versions available, see the [tags on this repository](https://github.com/alexandrac1420/Microframeworks_WEB.git).

## Authors

* **Alexandra Cortes Tovar** - [alexandrac1420](https://github.com/alexandrac1420)

## License

This project is licensed under the GNU
