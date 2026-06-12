# Mini Jira 

A Spring Boot-based task management system inspired by Jira, designed to manage tasks through swimlane workflows and event-driven notifications.

## Features

* Task Creation & Assignment
* Swimlane Workflow (TODO → IN_PROGRESS → DONE)
* Dynamic Task Filtering
* Role-Based Task Visibility
* Kafka-based Event Processing
* Feign Client Integration
* PostgreSQL Persistence
* Global Exception Handling

## Tech Stack

* Java
* Spring Boot
* PostgreSQL
* Apache Kafka
* FeignClient
* Gradle

Key Highlights :
Implemented Controller–Service–Repository architecture.
Built dynamic filtering APIs using optional query parameters.
Integrated Kafka for asynchronous event processing.
Used Feign Client for inter-service communication.
Enforced task visibility based on creator and assignee.
Added workflow validation for task state transitions.
Implemented centralized exception handling for consistent API responses.
## Author

Divyansh Mangal


## Run

```bash
.\gradlew.bat bootRun
```
