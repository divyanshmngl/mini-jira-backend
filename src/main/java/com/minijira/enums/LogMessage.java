package com.minijira.enums;

public enum LogMessage {

    TASK_CREATE_START("Starting task creation for userId={}"),
    TASK_CREATE_SUCCESS("Task created successfully taskId={} userId={}"),
    TASK_GET_START("Fetching task taskId={} userId={}"),
    TASK_GET_SUCCESS("Task fetched taskId={}"),
    TASK_FILTER_START("Filtering tasks userId={} started"),
    TASK_UPDATE_START("Updating task taskId={} userId={}"),
    TASK_UPDATE_SUCCESS("Task updated taskId={} swimlane={}"),
    TASK_MOVE_BLOCKED_NOTIFICATIONS("Move to DONE blocked, pending notifications taskId={} userId={}"),
    TASK_MOVE_INVALID("Invalid swimlane transition from={} to={} taskId={}"),
    TASK_TERMINAL_VIOLATION("Attempted move from terminal DONE taskId={}"),
    FETCH_VISIBLE_TASK("Fetching visible tasks for userId={}"),
    KAFKA_PUBLISH("Publishing task event taskId={} eventType={}"),
    KAFKA_CONSUME("Consumed notification event payload={}"),
    ACCESS_DENIED("Access denied taskId={} userId={}"),
    TASK_NOT_FOUND("Task not found taskId={}"),
    ERROR_OCCURRED("Error occurred operation={} message={}");

    private final String template;

    LogMessage(String template) {
        this.template = template;
    }

    public String getTemplate() {
        return template;
    }
}
