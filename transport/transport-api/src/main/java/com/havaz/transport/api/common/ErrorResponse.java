package com.havaz.transport.api.common;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * Error model for interacting with client.
 */
public class ErrorResponse {

    // HTTP Response Status Code
    private final HttpStatus status;

    // General Error message
    private final String message;

    // Error code
    private final ErrorCode errorCode;

    private final List<String> errors;

    private final LocalDateTime timestamp;

    protected ErrorResponse(final HttpStatus status, final String message, final ErrorCode errorCode, final List<String> errors) {
        this.message = message;
        this.errorCode = errorCode;
        this.status = status;
        if (errors == null) {
            this.errors = Collections.emptyList();
        } else {
            this.errors = errors;
        }
        this.timestamp = LocalDateTime.now();
    }

    protected ErrorResponse(final HttpStatus status, final String message, final ErrorCode errorCode) {
        this.message = message;
        this.errorCode = errorCode;
        this.status = status;
        this.errors = Collections.emptyList();
        this.timestamp = LocalDateTime.now();
    }

    public static ErrorResponse of(final String message, final ErrorCode errorCode,
                                   HttpStatus status) {
        return new ErrorResponse(status, message, errorCode);
    }

    public static ErrorResponse of(final String message, final ErrorCode errorCode,
                                   HttpStatus status, final List<String> errors) {
        return new ErrorResponse(status, message, errorCode, errors);
    }

    public static ErrorResponse of(final String message, final ErrorCode errorCode,
                                   HttpStatus status, final String error) {
        return new ErrorResponse(status, message, errorCode, Collections.singletonList(error));
    }


    public HttpStatus getStatus() {
        return status;
    }

    public Integer getStatusValue() {
        return status.value();
    }

    public String getMessage() {
        return message;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public List<String> getErrors() {
        return errors;
    }
}
