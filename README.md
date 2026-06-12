# Mini Jira 

A Spring Boot-based task management system inspired by Jira, designed to manage tasks through swimlane workflows and event-driven notifications.

## Features

* Task Creation & Assignment
* Swimlane Workflow (TODO → IN_PROGRESS → DONE)
* User-Based Task Visibility
* Dynamic Task Filtering
* Kafka-Based Task Event Publishing
* Notification Validation Before Task Completion
* PostgreSQL Persistence with JPA
* Global Exception Handling
* Structured Logging with SLF4J
* DTO-Driven REST APIs

## Tech Stack

* Java
* Spring Boot
* Spring Data Jpa
* PostgreSQL
* Apache Kafka
* Gradle
* Slf4j Logging

Key Highlights :
* Built a Spring Boot-based Mini Jira backend for task creation, assignment, update, and retrieval.
* Implemented swimlane workflow with transition validation for TODO, IN_PROGRESS, and DONE.
* Added user-based task visibility, allowing users to access only created or assigned tasks.
* Developed dynamic filtering APIs using native SQL for assignee, creator, swimlane, and date range.
* Integrated Kafka Producer to publish task events like task created, updated, and moved.
* Added notification validation before moving tasks to DONE.
* Used DTOs, enums, custom exceptions, global exception handling, and structured logging with @Slf4j.
* Followed clean Controller–Service–Repository architecture with Spring Data JPA.

## Author

Divyansh Mangal



