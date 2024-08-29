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
    [INFO] --- jar:3.3.0:jar (default-jar) @ AplicacionesDistriuidas ---
    [INFO] Building jar: C:\Users\alexa\OneDrive\Escritorio\Aplicaciones_Distribuidas\target\AplicacionesDistriuidas-1.0-SNAPSHOT.jar
    [INFO] BUILD SUCCESS
    ```

3. Run the application:
    ```sh
    java -cp target/Microframeworks_WEB-1.0-SNAPSHOT.jar edu.escuelaing.arep.SimpleWebServer

    ```
    When running the application, the following message should appear

    ```
    Ready to receive on port 8080...
    ```

    And now you can access `index.html` and other static files.

### Usage

1. **Static Files**: Access `index.html` and other static files (CSS, JavaScript, images) by navigating to `http://localhost:808index.html` in your web browser.

2. **Dynamic Greeting**: 
   - **GET Request**: Open `index.html` in your browser. You can input a name into the provided form. The server will send a GET request to `/app/hello?name=<YourName>` and respond with a JSON message and plain text greeting. For example, if you input "World", the response will include:
     - **JSON**: `{"nombre": "World"}`
     - **Plain Text**: `Hola, World`

3. **POST Request**: 
   - **POST Request**: Using a tool like Postman or a similar HTTP client, send a POST request to `http://localhost:8080/app/hello` with a payload containing a name. The server will respond with a message indicating the received input, e.g., `Post received: <YourName>`. For example, sending a POST request with the payload `{"name": "Jose Mario"}` will return `Post received: Jose Mario`.

    
## Architecture

![Architecture Diagram](https://github.com/alexandrac1420/Microframeworks_WEB/blob/master/Pictures/Arquitectura.png)

### Overview

The Web Framework is designed to handle concurrent HTTP client requests using a thread pool. It supports serving static files and processing REST API requests.

### Components

#### 1. **SimpleWebServer**
   - **Role**: Initializes a `ServerSocket` and listens for incoming client connections. Uses a thread pool (`ExecutorService`) for concurrent request handling.
   - **Responsibilities**:
     - Accept incoming client connections.
     - Delegate the handling of each client connection to a `ClientHandler`.
     - Manage the lifecycle of the server, including startup and shutdown.

#### 2. **ClientHandler**
   - **Role**: Processes individual client requests in separate threads.
   - **Responsibilities**:
     - Parse HTTP requests.
     - Serve static files or handle REST API requests.
     - Send HTTP responses, including error handling.
     - Close client socket after processing.

#### 3. **Request**
   - **Role**: Represents HTTP requests. Parses and stores request data.
   - **Responsibilities**:
     - Store HTTP method, URI, headers, and body.
     - Provide methods to access request data.

#### 4. **Response**
   - **Role**: Represents HTTP responses. Constructs and sends responses to clients.
   - **Responsibilities**:
     - Set HTTP status code, headers, and body.
     - Send response data to the client.

#### 5. **RestService**
   - **Role**: Abstract class/interface for REST API services.
   - **Responsibilities**:
     - Define structure for handling REST requests.
     - Provide methods for generating responses.

#### 6. **HelloService**
   - **Role**: Concrete implementation of `RestService` for the `/hello` endpoint.
   - **Responsibilities**:
     - Handle `/hello` requests and respond with a greeting.

### Interaction Flow

1. **Server Initialization**: The `SimpleWebServer` starts and initializes a `ServerSocket` on port 8080. It also sets up a thread pool with a fixed number of threads.

2. **Request Handling**:
   - When a client sends an HTTP request, the server accepts the connection and creates a new `ClientHandler` instance.
   - The `ClientHandler` reads the request, determines whether it’s a request for a static file or a REST-like API request, and processes it accordingly.
   - The appropriate response (file content, JSON response, or an error message) is sent back to the client.

3. **Concurrency**: Multiple client requests are handled concurrently by the thread pool, allowing the server to efficiently manage several connections at the same time.

4. **Shutdown**: When the server needs to be stopped, it gracefully shuts down the thread pool and closes the server socket.



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
This report outlines the unit tests conducted for the Web Framework project. Tests validated the server’s ability to handle REST services, serve static files, handle missing resources, and manage multiple concurrent connections.

### Tests Conducted

1. **Test `testHelloServiceResponse`**
   - **Description**: Validates the server’s handling of `/hello` requests.
   - **Objective**: Ensure correct greeting response for GET requests at `/app/hello`.
   - **Testing Scenario**: Clients simulate GET requests to `/app/hello?name=World`.
   - **Expected Behavior**: Response should include `HTTP/1.1 200 OK` and JSON with greeting.
   - **Verification**: Check response content for correctness.

2. **Test `testLoadStaticFile`**
   - **Description**: Checks serving of static files like `index.html`.
   - **Objective**: Validate correct serving of static files from the directory.
   - **Testing Scenario**: Request `index.html` from the server.
   - **Expected Behavior**: Response should include `HTTP/1.1 200 OK` and file content.
   - **Verification**: Confirm file is served correctly.

3. **Test `testInvalidRequest`**
   - **Description**: Tests response for non-existent files.
   - **Objective**: Ensure `404 Not Found` for missing resources.
   - **Testing Scenario**: Request a non-existent file like `nonexistentfile.html`.
   - **Expected Behavior**: Response should include `HTTP/1.1 404 Not Found` and an appropriate error message.
   - **Verification**: Confirm proper handling of missing resources with correct HTTP status.

4. **Test `testMultipleConnections`**
   - **Description**: Evaluates the server’s capability to handle multiple concurrent connections.
   - **Objective**: Ensure the server can process simultaneous requests without errors or significant performance issues.
   - **Testing Scenario**: Simulate multiple clients requesting `index.html` simultaneously.
   - **Expected Behavior**: The server should handle all requests, respond with `HTTP/1.1 200 OK`, and deliver the correct content for each request.
   - **Verification**: Verify performance and correctness under concurrent load conditions.

### Conclusion
The Web Framework has been thoroughly tested across various scenarios to ensure it meets expected behavior under different conditions. The tests confirm the server’s ability to handle both static file requests and dynamic REST services effectively, manage errors gracefully, and support multiple concurrent connections.


## Built With


* [Maven](https://maven.apache.org/) - Dependency Management
* [Git](http://git-scm.com/) - Version Control System

## Versioning

I use [GitHub](https://github.com/) for versioning. For the versions available, see the [tags on this repository](https://github.com/alexandrac1420/Aplicaciones_Distribuidas.git).

## Authors

* **Alexandra Cortes Tovar** - [alexandrac1420](https://github.com/alexandrac1420)

## License

This project is licensed under the GNU
