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
Built a Spring Boot-based Mini Jira backend for task creation, assignment, update, and retrieval.
Implemented swimlane workflow with transition validation for TODO, IN_PROGRESS, and DONE.
Added user-based task visibility, allowing users to access only created or assigned tasks.
Developed dynamic filtering APIs using native SQL for assignee, creator, swimlane, and date range.
Integrated Kafka Producer to publish task events like task created, updated, and moved.
Added notification validation before moving tasks to DONE.
Used DTOs, enums, custom exceptions, global exception handling, and structured logging with @Slf4j.
Followed clean Controller–Service–Repository architecture with Spring Data JPA.

## Author

Divyansh Mangal


## Run

```bash
.\gradlew.bat bootRun
```
