package com.minijira.utils;

import com.minijira.customException.TaskException;
import com.minijira.dto.JsonResponse;
import com.minijira.enums.MessageEnum;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private <T> ResponseEntity<JsonResponse<T>> buildResponse(HttpStatus status, String message, T data) {
        return new ResponseEntity<>(new JsonResponse<>(false, message, data), status);
    }

    @ExceptionHandler(TaskException.class)
    public ResponseEntity<JsonResponse<Void>> handleTaskException(TaskException ex, HttpServletRequest request) {
        MessageEnum messageEnum = ex.getMessageEnum();
        log.error("TaskException code={} at {}: {}", messageEnum.getCode(), request.getRequestURI(), ex.getMessage(), ex);
        return buildResponse(messageEnum.getHttpStatus(), ex.getMessage(), null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<JsonResponse<List<String>>> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(this::formatFieldError)
                .collect(Collectors.toList());
        log.warn("Validation failed: {}", errors);
        return buildResponse(HttpStatus.BAD_REQUEST, MessageEnum.VALIDATION_FAILED.getMessage(), errors);
    }

    private String formatFieldError(FieldError fieldError) {
        return String.format("%s: %s (rejected: %s)", fieldError.getField(), fieldError.getDefaultMessage(), fieldError.getRejectedValue());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<JsonResponse<Void>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String msg = String.format(MessageEnum.INVALID_PARAMETER_VALUE.getMessage(), ex.getName(), ex.getValue());
        log.info("Type mismatch: {}", msg);
        return buildResponse(MessageEnum.INVALID_PARAMETER_VALUE.getHttpStatus(), msg, null);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<JsonResponse<Void>> handleMissingParam(MissingServletRequestParameterException ex) {
        String msg = String.format(MessageEnum.MISSING_REQUIRED_PARAMETER.getMessage(), ex.getParameterName());
        log.warn("Missing parameter: {}", msg);
        return buildResponse(MessageEnum.MISSING_REQUIRED_PARAMETER.getHttpStatus(), msg, null);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<JsonResponse<Void>> handleNotReadable(HttpMessageNotReadableException ex) {
        log.warn("Malformed JSON request: {}", ex.getMessage());
        return buildResponse(MessageEnum.MALFORMED_JSON_REQUEST.getHttpStatus(), MessageEnum.MALFORMED_JSON_REQUEST.getMessage(), null);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<JsonResponse<Void>> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
        String msg = String.format(MessageEnum.METHOD_NOT_SUPPORTED.getMessage(), ex.getMethod());
        log.warn("Method not supported: {}", msg);
        return buildResponse(MessageEnum.METHOD_NOT_SUPPORTED.getHttpStatus(), msg, null);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<JsonResponse<String>> handleApiNotFound(NoResourceFoundException ex, HttpServletRequest request) {
        log.warn("apiNotFoundException at {}: {}", request.getRequestURI(), ex.getMessage());
        String details = String.format("No API mapping found for %s %s", ex.getHttpMethod(), ex.getResourcePath());
        return buildResponse(MessageEnum.API_NOT_FOUND.getHttpStatus(), MessageEnum.API_NOT_FOUND.getMessage(), details);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<JsonResponse<String>> handleAll(Exception ex, HttpServletRequest request) {
        log.error("Unhandled exception at {}: {}", request.getRequestURI(), ex.getMessage(), ex);
        return buildResponse(MessageEnum.UNEXPECTED_ERROR.getHttpStatus(), MessageEnum.UNEXPECTED_ERROR.getMessage(), ex.getMessage());
    }
}
