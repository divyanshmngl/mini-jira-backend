package com.minijira.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Error messages for exceptions and {@link com.minijira.utils.GlobalExceptionHandler}.
 * Always use {@link #getMessage()} in throws/handlers (not {@link #name()} unless returning an error code).
 */
@Getter
public enum MessageEnum {

    TASK_NOT_FOUND("Task not found", HttpStatus.NOT_FOUND),
    ACCESS_DENIED("You do not have access to this task", HttpStatus.FORBIDDEN),
    INVALID_SWIMLANE_TRANSITION("Swimlane transition is not allowed", HttpStatus.BAD_REQUEST),
    TERMINAL_STATE_VIOLATION("Task in DONE cannot be moved", HttpStatus.CONFLICT),
    NOTIFICATIONS_PENDING("Unread notifications must be acknowledged before moving to DONE", HttpStatus.CONFLICT),
    OPTIMISTIC_LOCK_FAILURE("Task was modified by another user, please retry", HttpStatus.CONFLICT),
    VALIDATION_FAILED("Validation failed", HttpStatus.BAD_REQUEST),
    USER_NOT_AUTHORISED("User not authorised", HttpStatus.BAD_REQUEST),
    NOTIFICATION_SERVICE_UNAVAILABLE("Notification service is unavailable", HttpStatus.SERVICE_UNAVAILABLE),
    UNEXPECTED_ERROR("Unexpected error", HttpStatus.INTERNAL_SERVER_ERROR),
    MALFORMED_JSON_REQUEST("Malformed JSON request", HttpStatus.BAD_REQUEST),
    API_NOT_FOUND("API not found", HttpStatus.NOT_FOUND),
    METHOD_NOT_SUPPORTED("Method %s not supported", HttpStatus.METHOD_NOT_ALLOWED),
    MISSING_REQUIRED_PARAMETER("Missing required parameter '%s'", HttpStatus.BAD_REQUEST),
    INVALID_PARAMETER_VALUE("Parameter '%s' has invalid value '%s'", HttpStatus.BAD_REQUEST),
    INVALID_TITLE("Title is Invalid",HttpStatus.BAD_REQUEST);

    private final String message;
    private final HttpStatus httpStatus;

    MessageEnum(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public String getCode() {
        return name();
    }
}
