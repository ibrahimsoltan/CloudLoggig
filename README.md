# TestLog Project

## Overview
The TestLog project is designed to demonstrate advanced logging techniques using Log4J2, integrating with Elasticsearch, and leveraging Spring Boot's powerful features. This project contains custom components to facilitate logging directly to Elasticsearch and managing application contexts dynamically.

## Components

### `ApplicationContextProvider`
- **Purpose**: Manages the Spring application context globally, allowing access to beans from non-Spring-managed objects.
- **Details**: This class implements `ApplicationContextAware` and provides a static method to retrieve beans from the Spring application context, ensuring that Spring-managed beans can be accessed throughout the application.

### `ElasticsearchCustomAppender`
- **Purpose**: Custom Log4J2 appender for sending log messages directly to an Elasticsearch instance.
- **Details**: Extends `AbstractAppender` from Log4J2, utilizing a custom service to format and dispatch log entries to Elasticsearch. It ensures thread-safe lazy initialization of the logging service.

### `ElasticsearchLoggingService`
- **Purpose**: Service layer that handles the construction of Elasticsearch payloads and communicates with the Elasticsearch server.
- **Details**: Uses Spring's `WebClient` to post log data asynchronously to Elasticsearch, formatting each log entry as a JSON object.

### `Log4jConfigFactory`
- **Purpose**: Provides a Log4J2 configuration programmatically, bypassing the need for XML configuration files.
- **Details**: Implements Log4J2’s `ConfigurationFactory` to build and provide a custom configuration that includes the `ElasticsearchCustomAppender` and standard console output.

### `Log4jInitializer`
- **Purpose**: Configures Log4J2 upon application startup using Spring's event listeners.
- **Details**: Implements `ApplicationListener` to react to the `ApplicationReadyEvent`, setting up Log4J2 with a specific configuration defined programmatically.

### `TestLogApplication`
- **Purpose**: Main entry point for the Spring Boot application.
- **Details**: Contains the main method that triggers the Spring Boot application, also responsible for setting system properties relevant to Log4J2 configuration.

### `TestLogging`
- **Purpose**: Demonstrates various logging levels and messages.
- **Details**: Contains methods to log messages at different severity levels, which helps in demonstrating the output capabilities of the configured log system.

## Getting Started
To run this project, ensure you have Maven and Java 17+ installed. Clone the repository, navigate to the project directory, and run:
```bash
mvn spring-boot:run
