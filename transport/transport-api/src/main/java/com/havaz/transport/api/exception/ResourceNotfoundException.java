package com.havaz.transport.api.exception;

import com.havaz.transport.core.exception.TransportException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public final class ResourceNotfoundException extends TransportException {

    private static final long serialVersionUID = -5249566566336713957L;

    public ResourceNotfoundException(String message) {
        super(message);
    }

    public ResourceNotfoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResourceNotfoundException(Throwable cause) {
        super(cause);
    }

    protected ResourceNotfoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
